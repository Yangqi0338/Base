package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.AdminDisableAccountReq;
import com.newzkl.platform.base.biz.account.model.req.AdminRegisterIdentityReq;
import com.newzkl.platform.base.biz.account.model.req.EmpRegisterReq;
import com.newzkl.platform.base.biz.account.model.req.EmpSaveCommand;
import com.newzkl.platform.base.biz.account.model.vo.EmpAccountVO;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.biz.account.model.vo.MemberAccountVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户-员工
 *
 * <p>员工无独立业务字段, 等价于 {@code identity=EMP} 的账号集合。故本控制器不引入 emp 专属存储,
 * 增删改查全部复用账号中台能力: 列表/新增/禁用走 {@link AccountService}, 修改走 {@link AccountDomain}。
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
    private final AccountDomain accountDomain;

    /**
     * 员工分页
     *
     * @param query 账号查询
     * @return 员工账号分页
     */
    @PostMapping("/page")
    @FuncPermission("分页员工")
    public PlatformResult<Page<EmpAccountVO>> page(@RequestBody AccountQuery query) {
        query.setClient(AccountEnum.Client.ADMIN);
        query.setIdentity(AccountEnum.Identity.EMP);
        return PlatformResult.success((Page<EmpAccountVO>) accountService.pageAccount(query));
    }

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
     * @param req 账号修改请求
     * @return 空结果
     */
    @PostMapping("/edit")
    @FuncPermission("修改员工")
    public PlatformResult<Void> edit(@RequestBody EmpSaveCommand command) {
        CommonUtil.validate(command);
        if (command.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM, "缺少ID");
        }
        AccountReq req = TransferUtils.transfer(command, AccountReq.class);
        req.setIdentity(AccountEnum.Identity.EMP);
        accountDomain.accountEdit(req);
        return PlatformResult.success();
    }
}
