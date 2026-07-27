package com.newzkl.platform.base.biz.sys.action.controller;

import com.newzkl.platform.base.biz.sys.domain.service.AdminAccountDomain;
import com.newzkl.platform.base.biz.sys.model.adminaccount.query.AdminAccountQuery;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.AdminAccountReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.PasswordUpdateReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.res.AdminAccountRes;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台-账号控制器。
 *
 * <p>[AUTH] 登录 {@code passwordLogin} + token 签发 + 当前登录 id 注入 + {@code @EnableIdParse}
 * 脱敏均归入口 starter (building-scm), 详见 findings 鉴权下沉。本控制器仅账号 CRUD + 改密。
 * {@code detail}/{@code passwordUpdate} 的当前登录 id 改为显式入参 (入口过滤器注入)。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class AdminAccountController {

    private final AdminAccountDomain adminAccountDomain;

    /**
     * 创建账号。
     *
     * @param req 账号请求
     * @return 账号 id
     */
    @PostMapping("/createAccount")
    public PlatformResult<Long> createAccount(@Validated @RequestBody AdminAccountReq req) {
        return PlatformResult.success(adminAccountDomain.adminAccountCreate(req));
    }

    /**
     * 删除账号。
     *
     * @param req id 列表入参
     * @return 成功结果
     */
    @PostMapping("/delete")
    public PlatformResult<Void> delete(@Validated @RequestBody IdListCommand req) {
        adminAccountDomain.adminAccountDelete(req.getIdList());
        return PlatformResult.success();
    }

    /**
     * 修改账号。
     *
     * @param req 账号请求 (id 必填)
     * @return 成功结果
     */
    @PostMapping("/update")
    public PlatformResult<Void> update(@Validated(UpdateCommand.class) @RequestBody AdminAccountReq req) {
        adminAccountDomain.adminAccountUpdate(req);
        return PlatformResult.success();
    }

    /**
     * 账号详情。
     *
     * @param id 账号 id (入口按当前登录态注入; 缺省时由入口填充当前登录 id)
     * @return 账号视图对象
     */
    @PostMapping("/detail")
    public PlatformResult<AdminAccountRes> detail(@RequestParam("id") Long id) {
        return PlatformResult.success(adminAccountDomain.adminAccountVO(id));
    }

    /**
     * 账号分页列表。
     *
     * @param query 账号查询
     * @return 账号列表
     */
    @PostMapping("/page")
    public PlatformResult<List<AdminAccountRes>> page(@RequestBody AdminAccountQuery query) {
        return PlatformResult.success(adminAccountDomain.adminAccountList(query));
    }

    /**
     * 密码修改 (当前登录 accountId 由入口注入到入参)。
     *
     * @param req 密码修改请求
     * @return 成功结果
     */
    @PostMapping("/passwordUpdate")
    public PlatformResult<Void> passwordUpdate(@Validated @RequestBody PasswordUpdateReq req) {
        adminAccountDomain.passwordUpdate(req);
        return PlatformResult.success();
    }
}
