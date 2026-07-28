package com.newzkl.platform.base.biz.auth.action.controller;

import com.newzkl.platform.base.biz.auth.domain.service.RoleDomain;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleReq;
import com.newzkl.platform.base.biz.auth.model.role.res.RoleRes;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色查询 / 维护控制器。
 *
 * <p>原 {@code com.zkl.scm.user.interfaces.controller.RoleController} 的角色主体端点部分。
 * biz-auth 切分时从 biz-account 的 {@code RoleController} 拆出: 仅依赖 {@link RoleDomain} 的
 * roleList / roleDetail / roleListSave 三端点迁入本域; 其余端点 (applyRole / saveApplyCommand /
 * loadApplyCommand / submitPromiseFlow / cdkList / toCdk) 依赖账号注册编排与开通码 (CDK),
 * 留在 biz-account 的 {@code RoleController}。</p>
 *
 * <p>路径与 HTTP 方法保持不变 (仍挂 {@code /user/role}), 前端契约不受影响; 仅类名区别于
 * biz-account 侧同基址控制器, 避免 Spring bean 名冲突。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/user/role")
@RequiredArgsConstructor
public class RoleQueryController {

    private final RoleDomain roleDomain;

    /**
     * 角色列表。
     *
     * @param roleQuery 角色查询
     * @return 角色列表
     */
    @PostMapping("/roleList")
    public PlatformResult<List<RoleRes>> roleList(@RequestBody RoleQuery roleQuery) {
        return PlatformResult.success(roleDomain.list(roleQuery));
    }

    /**
     * 角色详情。
     *
     * @param roleId 角色 ID
     * @return 角色详情, 无则 null
     */
    @GetMapping("/roleDetail")
    public PlatformResult<RoleRes> roleDetail(@RequestParam("roleId") Long roleId) {
        return PlatformResult.success(roleDomain.detail(roleId));
    }

    /**
     * 角色保存。
     *
     * <p>保留旧语义: 请求体带 id 走修改, 否则走新建。</p>
     *
     * @param roleReq 角色入参
     * @return 成功结果
     */
    @PostMapping("/roleListSave")
    public PlatformResult<Void> roleListSave(@RequestBody RoleReq roleReq) {
        if (roleReq.getId() != null) {
            roleDomain.edit(roleReq.getId(), roleReq);
        } else {
            roleDomain.save(roleReq);
        }
        return PlatformResult.success();
    }
}
