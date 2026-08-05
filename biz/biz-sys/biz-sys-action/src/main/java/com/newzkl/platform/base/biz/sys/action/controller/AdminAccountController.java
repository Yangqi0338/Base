package com.newzkl.platform.base.biz.sys.action.controller;

import com.newzkl.platform.base.biz.sys.domain.service.AdminAccountDomain;
import com.newzkl.platform.base.biz.sys.model.adminaccount.query.AdminAccountQuery;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.AdminAccountReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.PasswordUpdateReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.res.AdminAccountRes;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台-账号控制器
 *
 * <p>迁移说明: 源 {@code AdminAccountController} 直连 {@code IAdminAccountRepository} +
 * {@code AdminAccountAssembler} 并在 action 层做 BCrypt 加密, 违反 action 不触达持久化/密码逻辑
 * 的红线; 此处统一走 {@code AdminAccountDomain}, 加密下沉 domain。入参 {@code AdminAccountVO}
 * 拆分为 {@code AdminAccountReq}/{@code AdminAccountRes}。</p>
 *
 * <p>[AUTH] 源 {@code passwordLogin} (SaToken token 签发) 归入口 starter (building-scm), 不迁本域;
 * {@code detail}/{@code passwordUpdate} 的当前登录 id 由入口 {@code SecurityContextFilter} 注入,
 * 本域经 读取。源 {@code page} 上 {@code @EnableIdParse}
 * (角色 id→名称脱敏回填) 属入口关注, 已剥离。</p>
 *
 * <p>响应壳变更: 源 {@code page} 返回 PageHelper {@code PageInfo}, 本域降为
 * {@code List<AdminAccountRes>} (分页在 RepositoryImpl 内 MyBatis-Plus selectPage, 不外泄 Page)。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class AdminAccountController {

    private final AdminAccountDomain adminAccountDomain;

    /**
     * 账号创建
     *
     * @param req 账号请求 (明文密码, domain 内 BCrypt 加密落库)
     * @return 账号 id
     */
    @PostMapping("createAccount")
    public PlatformResult<Long> createAccount(@Validated @RequestBody AdminAccountReq req) {
        return PlatformResult.success(adminAccountDomain.adminAccountCreate(req));
    }

    /**
     * 账号删除
     *
     * @param idList 账号 id 列表
     * @return 空结果
     */
    @PostMapping("delete")
    public PlatformResult<Void> delete(@Validated @RequestBody IdListCommand idList) {
        adminAccountDomain.adminAccountDelete(idList.getIdList());
        return PlatformResult.success();
    }

    /**
     * 账号修改
     *
     * @param req 账号请求 (id 必填, 传密码则重新加密)
     * @return 空结果
     */
    @PostMapping("update")
    public PlatformResult<Void> update(@Validated({UpdateCommand.class, Default.class}) @RequestBody AdminAccountReq req) {
        adminAccountDomain.adminAccountUpdate(req);
        return PlatformResult.success();
    }

    /**
     * 账号详情
     *
     * <p>入参为空时取当前登录账号 (入口注入)。</p>
     *
     * @param idList 账号 id 列表 (可空; 取首个)
     * @return 账号详情
     */
    @PostMapping("detail")
    public PlatformResult<AdminAccountRes> detail(@RequestBody(required = false) IdListCommand idList) {
        Long accountId = (idList == null || idList.getId() == null)
                ? SecurityUtils.getAccountId()
                : idList.getId();
        return PlatformResult.success(adminAccountDomain.adminAccountVO(accountId));
    }

    /**
     * 账号列表
     *
     * @param query 账号查询
     * @return 账号列表
     */
    @PostMapping("page")
    public PlatformResult<List<AdminAccountRes>> page(@RequestBody AdminAccountQuery query) {
        return PlatformResult.success(adminAccountDomain.adminAccountList(query));
    }

    /**
     * 密码修改
     *
     * <p>[AUTH] {@code accountId} 由入口注入当前登录态。</p>
     *
     * @param req 密码修改请求
     * @return 空结果
     */
    @PostMapping("passwordUpdate")
    public PlatformResult<Void> passwordUpdate(@Validated @RequestBody PasswordUpdateReq req) {
        req.setAccountId(SecurityUtils.getAccountId());
        adminAccountDomain.passwordUpdate(req);
        return PlatformResult.success();
    }
}
