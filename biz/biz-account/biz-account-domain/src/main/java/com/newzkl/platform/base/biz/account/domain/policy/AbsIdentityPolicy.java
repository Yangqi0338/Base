package com.newzkl.platform.base.biz.account.domain.policy;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PermissionApi;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 角色策略
 * @date 2024/1/911:42
 */
@Component
public abstract class AbsIdentityPolicy {

    @Autowired
    private PermissionApi permissionApi;

    public abstract AccountEnum.Identity support();

    /**
     * 绑定账号-角色
     *
     * <p>多身份通用能力: 建号后全量替换该账号在本身份所属端下的角色集合。端由 {@link #support()} 推导,
     * 角色须同端。空集视为不绑定, 不清空既有角色。</p>
     *
     * @param accountId  账号 id
     * @param roleIdList 角色 id 集合
     */
    protected void bindRoles(Long accountId, List<Long> roleIdList) {
        if (CollUtil.isEmpty(roleIdList)) {
            return;
        }
        permissionApi.bindRoles(support().getClient(), accountId, roleIdList);
    }

    /**
     * 解绑账号-角色
     *
     * <p>注销时清空该账号在本身份所属端下的全部角色, 底层以空集全量替换实现。端由 {@link #support()} 推导</p>
     *
     * @param accountId 账号 id
     */
    protected void unbindRoles(Long accountId) {
        permissionApi.bindRoles(support().getClient(), accountId, Collections.emptyList());
    }

    /**
     * 默认身份自动授超管
     *
     * <p>注册身份等于所属端 defaultIdentityCode 时, 追加绑定该端 SUPER_ADMIN 角色,
     * 使自助注册的端主账号开箱即拥有全部权限。非默认身份(如后续引入的员工)不触发,
     * 改由管理员分配角色。端无 SUPER_ADMIN 角色则静默跳过</p>
     *
     * @param accountId 账号 id
     */
    public void autoBindDefaultSuperAdmin(Long accountId) {
        AccountEnum.Identity identity = support();
        AccountEnum.Client client = identity.getClient();
        if (identity.getCode().equals(client.getDefaultIdentityCode())) {
            permissionApi.bindSuperAdmin(client, accountId);
        }
    }

    /**
     * 个人注册角色
     * @param customSaveReq
     * @return
     */
    public abstract IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq);

    public abstract boolean destroy(AccountVO accountVO, String destroyReason);

    public abstract void saveByAccount(AccountReq req);
}
