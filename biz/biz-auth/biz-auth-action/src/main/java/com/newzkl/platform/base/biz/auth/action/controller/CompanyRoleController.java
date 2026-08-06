package com.newzkl.platform.base.biz.auth.action.controller;

import com.newzkl.platform.base.biz.auth.domain.service.RoleDomain;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleReq;
import com.newzkl.platform.base.biz.auth.model.role.res.RoleRes;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户-企业角色 (可申请角色配置)
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.RoleController} 中
 * 落在 {@code role} 表上的三个端点 ({@code roleList} / {@code roleDetail} / {@code roleListSave})。
 * 类级路径 {@code /user/role} 与方法级路径逐字沿用旧契约, {@code roleDetail} 保持
 * {@code GET + @RequestParam("roleId")}, 不得按 REST 习惯改写为 POST。</p>
 *
 * <p>归属说明: {@code role} 表的 DO / DAO / Repository / Domain 全在 biz-auth,
 * 故这三条落 biz-auth-action; 同域 {@code /user/role} 下的其他端点 (申请角色 / 兑换码等)
 * 落在 biz-account-action 的 {@code RoleController}, 两个类共用类级路径但方法路径不重叠,
 * Spring 可正常注册。类名不叫 {@code RoleController} 是因为 Base 全包扫描下
 * bean 简单名必须全局唯一。</p>
 *
 * <p>能力缺失: 旧出参 {@code RoleVO} 的 {@code dataGroupNameList} / {@code dataGroupVOList}
 * 由 admin 域 {@code IDataGroupFacade} 回填, 资料组能力未在 Base 落地, 故这两个字段不回填
 * (前端 {@code platform-admin/src/views/role/roleList.vue} 的「提交资料」列会空),
 * 见迁移报告「能力缺失」。旧 {@code @Limit(code=company_role, level=set)} 未迁移。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/role")
@RequiredArgsConstructor
public class CompanyRoleController {

    private final RoleDomain roleDomain;

    /**
     * 角色列表
     *
     * <p>出参形态沿用旧契约: 裸列表, 非分页包装。旧 {@code rolePage} 仅在
     * {@code pageNo > 0} 时启用分页, 该语义已由 {@link RoleDomain#list} 保留</p>
     *
     * @param roleQuery 角色查询
     * @return 角色列表
     */
    @PostMapping("/roleList")
    public PlatformResult<List<RoleRes>> roleList(@RequestBody RoleQuery roleQuery) {
        return PlatformResult.success(roleDomain.list(roleQuery));
    }

    /**
     * 角色详情
     *
     * @param roleId 角色 ID
     * @return 角色详情, 不存在返回 null
     */
    @GetMapping("/roleDetail")
    public PlatformResult<RoleRes> roleDetail(@RequestParam("roleId") Long roleId) {
        return PlatformResult.success(roleDomain.detail(roleId));
    }

    /**
     * 角色保存
     *
     * <p>保留旧语义: 带 {@code id} 走修改, 否则走新增; 修改以入参 {@code id} 为目标</p>
     *
     * @param roleReq 角色入参
     * @return 空结果
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
