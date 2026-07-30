package com.newzkl.platform.base.biz.auth.application.provider;

import com.newzkl.platform.base.biz.auth.domain.adapt.repository.AuthRepository;
import com.newzkl.platform.base.biz.auth.domain.service.RoleDomain;
import com.newzkl.platform.base.biz.auth.facade.AuthApi;
import com.newzkl.platform.base.biz.auth.facade.model.RoleInfo;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.role.res.RoleRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * {@code AuthApi} 的本域实现
 *
 * <p>把 biz-auth 内部的 {@code RoleDomain} / {@code AuthRepository} 出参转换为 facade 传输对象,
 * 使其他域无需引用 biz-auth-model 即可查角色。</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthApiProvider implements AuthApi {

    private final RoleDomain roleDomain;
    private final AuthRepository authRepository;

    @Override
    public RoleInfo queryRole(Long roleId) {
        if (roleId == null) {
            return null;
        }
        return toRoleInfo(roleDomain.detail(roleId));
    }

    @Override
    public List<RoleInfo> queryRoles(List<Long> roleIdList) {
        if (roleIdList == null || roleIdList.isEmpty()) {
            return Collections.emptyList();
        }
        RoleQuery query = new RoleQuery();
        query.setIdList(roleIdList);
        List<RoleRes> resList = roleDomain.list(query);
        if (resList == null || resList.isEmpty()) {
            return Collections.emptyList();
        }
        return resList.stream().map(this::toRoleInfo).toList();
    }

    @Override
    public void refreshUnPermissionCache(Long accountId) {
        if (accountId == null) {
            return;
        }
        authRepository.cacheUserUnFunctionUrls(accountId);
    }

    /**
     * 内部出参转 facade 传输对象
     *
     * @param res 角色出参
     * @return facade 角色信息, 入参为 null 时返回 null
     */
    private RoleInfo toRoleInfo(RoleRes res) {
        if (res == null) {
            return null;
        }
        RoleInfo info = new RoleInfo();
        info.setId(res.getId());
        info.setName(res.getName());
        info.setTotalUserNum(res.getTotalUserNum());
        info.setDes(res.getDes());
        return info;
    }
}
