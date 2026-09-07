package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.service.AdminClientDomain;
import com.newzkl.platform.base.biz.account.model.dto.EmpDTO;
import com.newzkl.platform.base.biz.account.model.req.AdminRegisterIdentityReq;
import com.newzkl.platform.base.biz.account.model.req.EmpRegisterReq;
import com.newzkl.platform.base.biz.account.model.req.EmpSaveCommand;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;
import com.newzkl.platform.base.common.core.model.check.UpdateCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户-员工
 *
 * <p>员工自有列极少: {@code emp} 表只有员工类型一列, 登录凭证与主子关系全在 {@code account} 表。
 * 故新增走 {@link AccountService} 注册链, 修改与详情走 {@link AdminClientDomain} 同事务落两表。
 * 列表查询已统一到 {@code POST /user/account/aggPage} (identity = EMP), 不再在此维护。
 * 身份与端在控制器内固定 (identity=EMP, client=ADMIN), 前端无需传角色。</p>
 *
 * <p>与旧契约差异: 旧 {@code EmpController} 含登录/Excel 导入, 已由账号中台与批量导入端点承接, 本类不再重复。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/user/emp")
@RequiredArgsConstructor
@RoleLimit(client = AccountEnum.Client.ADMIN)
@FuncPermission("员工管理")
public class EmpController {

    private final AccountService accountService;
    private final AdminClientDomain adminClientDomain;

    /**
     * 员工新增
     *
     * <p>收员工专用入参 {@link EmpRegisterReq}, 端内转 {@link AdminRegisterIdentityReq}:
     * identity 固定 EMP, 角色由 roleIdList 指定并在注册链绑定。</p>
     *
     * @param req 员工注册请求
     * @return 新建账号 ID
     */
    @PostMapping("/create")
    @FuncPermission("新建员工")
    public PlatformResult<Long> create(@RequestBody @Validated EmpRegisterReq req) {
        log.info("新增员工");
        AdminRegisterIdentityReq registerReq = TransferUtils.transfer(req, AdminRegisterIdentityReq.class);
        registerReq.setIdentity(AccountEnum.Identity.EMP);
        return PlatformResult.success(accountService.identityCreate(registerReq));
    }

    /**
     * 员工修改
     *
     * @param command 员工修改请求
     * @return 空结果
     * @ext 主数据 emp, 副数据 account(单副, 副数据不再向下关联)。{@code type} 落 emp 表,
     *      其余字段(昵称/真实姓名/手机号/头像/父id)落 account 表
     */
    @PostMapping("/edit")
    @FuncPermission("修改员工")
    public PlatformResult<Void> edit(@Validated(UpdateCommand.class) @RequestBody EmpSaveCommand command) {
        adminClientDomain.empEdit(command.getId(), command);
        return PlatformResult.success();
    }

    /**
     * 员工纯净详情
     *
     * <p>只需员工类型时走本端点, 相比 {@code detail} 少一次 account 查询</p>
     *
     * @param id 员工账号ID
     * @return 员工纯净视图
     * @ext 主数据 emp (无副数据)
     */
    @GetMapping("/base")
    public PlatformResult<EmpDTO> base(@RequestParam("id") Long id) {
        return PlatformResult.success(adminClientDomain.empBase(id));
    }

    /**
     * 员工详情
     *
     * @param id 员工账号ID
     * @return 员工聚合视图
     * @ext 主数据 emp, 副数据 account(单副, 副数据不再向下关联)。方向与
     *      {@code AccountController.identityDetail}(主 account / 副身份) 相反, 两者不可互相替代
     */
    @GetMapping("/detail")
    public PlatformResult<EmpRes> detail(@RequestParam("id") Long id) {
        return PlatformResult.success(adminClientDomain.empDetail(id));
    }
}
