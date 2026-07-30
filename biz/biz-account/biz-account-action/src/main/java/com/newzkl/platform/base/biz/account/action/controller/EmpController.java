package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.action.cmd.EmpCmd;
import com.newzkl.platform.base.biz.account.application.auth.service.AccountLoginService;
import com.newzkl.platform.base.biz.account.domain.service.AdminClientDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.EmpPasswordLoginReq;
import com.newzkl.platform.base.biz.account.model.auth.req.LoginReq;
import com.newzkl.platform.base.biz.account.model.auth.res.LoginRes;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.req.EmpCreateReq;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

/**
 * 用户-员工
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.EmpController}。
 * 类级路径与方法级路径逐字沿用旧契约, 含旧代码中不带前导斜杠的写法 (如 {@code empCreate} / {@code empPage})。</p>
 *
 * <p>迁移补充: 旧 {@code EmpDomain} 的登录 / 删除 / 修改与 Excel 导入四项能力已补齐。
 * 关键结构差异: 旧 {@code emp} 表自带 {@code username} / {@code password} / {@code account_id},
 * 一行即一员工; Base 拆成 {@code account} (凭证 + 主子关系) 与 {@code emp} (类型 + 岗位)
 * 两行同主键, 故新增与删除都涉两表, 登录改走账号侧
 * {@link AccountLoginService#subPasswordLogin}。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/emp")
@RequiredArgsConstructor
public class EmpController {

    /**
     * 主账号登录标记: 旧契约用 {@code parentUsername="0"} 表示登录主账号自身
     */
    private static final String MAIN_LOGIN_PARENT = "0";

    private final AdminClientDomain adminClientDomain;
    private final AccountLoginService accountLoginService;

    /**
     * 员工新增
     *
     * <p>迁移补充: 旧 {@code EmpDomain.empCreate(req, accountId)} 中台化后只保留批量入口
     * {@link AdminClientDomain#batchEmpCreate}, 此处按单元素集合调用, 语义等价。
     * 旧 {@code @Limit(code=1026, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param empCreateReq 员工新增请求
     * @return 空结果
     */
    @PostMapping("empCreate")
    public PlatformResult<Void> empCreate(@Validated @RequestBody EmpCreateReq empCreateReq) {
        adminClientDomain.batchEmpCreate(Collections.singletonList(empCreateReq), SecurityUtils.getAccountId());
        return PlatformResult.success();
    }

    /**
     * 员工列表
     *
     * <p>迁移补充: 中台出参为 {@code EmpRes}。
     * 旧 {@code @Limit(code=1026, level=get)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param empQuery 员工查询
     * @return 员工分页
     */
    @PostMapping("empPage")
    public PlatformResult<Page<EmpRes>> empPage(@RequestBody EmpQuery empQuery) {
        empQuery.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(adminClientDomain.empPage(empQuery));
    }

    /**
     * 员工删除
     *
     * <p>入参形态逐字沿用旧契约: 直接收裸 {@code List<Long>} 请求体, 不包 {@code IdListCommand}。
     * 迁移补充: 旧只删 {@code emp} 单表, Base 下凭证在 {@code account}, 只删 {@code emp}
     * 会留可登录的孤儿账号, 故 {@link AdminClientDomain#empDelete} 两表同删。
     * 旧 {@code @Limit(code=1026, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param empIdList 员工 ID 列表
     * @return 空结果
     */
    @PostMapping("empDelete")
    public PlatformResult<Void> empDelete(@RequestBody List<Long> empIdList) {
        adminClientDomain.empDelete(empIdList);
        return PlatformResult.success();
    }

    /**
     * 员工修改
     *
     * <p>保留旧语义: 缺 ID 抛参数异常。
     * 旧 {@code @Limit(code=1026, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param empCreateReq 员工修改请求
     * @return 空结果
     */
    @PostMapping("empEdit")
    public PlatformResult<Void> empEdit(@RequestBody EmpCreateReq empCreateReq) {
        if (empCreateReq.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM, "缺少ID");
        }
        adminClientDomain.empEdit(empCreateReq);
        return PlatformResult.success();
    }

    /**
     * 员工密码登录
     *
     * <p>保留旧鉴权语义: 平台角色且 {@code parentUsername} 为 {@code "0"} 时按主账号登录
     * (旧 SQL {@code account_id = 0} 分支), 其余一律按 {@code parentUsername} 定位主账号后
     * 登录其子账号 (旧 emp-emp / emp-account 两条 join 在 Base 拆表后同为「主账号 + 子账号名」)。</p>
     *
     * <p>迁移补充: 旧登录直接在 {@code emp} 表比对密码并自行拼 sa-token; Base 收敛到
     * {@code AccountLoginService}, 端固定为 {@code ADMIN} (旧代码也是无条件写
     * {@code DETAILS_CLIENT=ADMIN}), 登录类型固定为密码。
     * 旧出参 {@code LoginRes.accountType} 由 {@code emp.type} 决定, Base {@code LoginRes}
     * 无该字段, 未回填, 见迁移报告「能力缺失」。</p>
     *
     * @param passwordLoginReq 员工密码登录请求
     * @return 登录结果
     */
    @PostMapping("/passwordLogin")
    public PlatformResult<LoginRes> passwordLogin(@Validated @RequestBody EmpPasswordLoginReq passwordLoginReq) {
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername(passwordLoginReq.getUsername());
        loginReq.setPassword(passwordLoginReq.getPassword());
        loginReq.setClient(CommonEnum.Client.ADMIN);
        loginReq.setType(AccountEnum.LoginType.PASSWORD);

        boolean isMainLogin = RoleEnum.CompanyRole.PLATFORM.getCode().equals(SecurityUtils.getRoleId())
                && MAIN_LOGIN_PARENT.equals(passwordLoginReq.getParentUsername());
        if (isMainLogin) {
            loginReq.setMainAccountId(AccountEnum.MAIN_ACCOUNT_PID);
            return PlatformResult.success(accountLoginService.accountLogin(loginReq));
        }
        return PlatformResult.success(accountLoginService.subPasswordLogin(passwordLoginReq.getParentUsername(), loginReq));
    }

    /**
     * Excel新增员工
     *
     * <p>入参形态逐字沿用旧契约: 请求体 {@code {"string": "<excel地址>"}}。
     * 迁移补充: 旧用 {@code EasyExcel.read(...).sheet().doRead()} 配
     * {@code OpenSubAccountListener} 边读边落库, Base 统一走
     * {@link com.newzkl.platform.base.common.core.utils.common.EasyExcelUtil#importBiz}
     * 先校验再批量落库, 出参对象为 {@code EasyExcelErrorVO}。
     * 旧 {@code @Limit(code=1026, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param excelUrl Excel 地址入参
     * @return 导入错误信息
     * @throws IOException 远端 Excel 读取失败
     */
    @PostMapping("/excelCreateEmp")
    public PlatformResult<EasyExcelErrorVO> excelCreateEmp(@Validated @RequestBody EmpCmd.ExcelUrl excelUrl) throws IOException {
        URLConnection connection = URI.create(excelUrl.getString()).toURL().openConnection();
        try (InputStream inputStream = connection.getInputStream()) {
            return PlatformResult.success(adminClientDomain.excelCreateEmp(inputStream, SecurityUtils.getAccountId()));
        }
    }
}
