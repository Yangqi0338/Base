package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.PermissionApi;
import com.newzkl.platform.base.biz.auth.facade.RoleFacade;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleApiImpl implements PermissionApi {

    @RpcReference
    private RoleFacade roleFacade;

    @Override
    public Map<Long, List<Long>> findRoleByAccountIdList(AccountEnum.Client client, List<Long> accountIdList) {
        return roleFacade.findRoleByAccount(client, accountIdList);
    }

    @Override
    public void bindRoles(AccountEnum.Client client, Long accountId, List<Long> roleIdList) {
        roleFacade.bindRoles(client, accountId, roleIdList);
    }

    @Override
    public void bindSuperAdmin(AccountEnum.Client client, Long accountId) {
        roleFacade.bindSuperAdmin(client, accountId);
    }
}
