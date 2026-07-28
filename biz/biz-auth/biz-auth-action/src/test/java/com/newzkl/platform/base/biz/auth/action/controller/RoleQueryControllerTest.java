package com.newzkl.platform.base.biz.auth.action.controller;

import com.newzkl.platform.base.biz.auth.domain.service.RoleDomain;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleReq;
import com.newzkl.platform.base.biz.auth.model.role.res.RoleRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link RoleQueryController} 入参与出参契约测试。
 *
 * <p>原 biz-account {@code RoleControllerTest} 的角色主体用例部分, 随 roleList / roleDetail /
 * roleListSave 三端点迁入 biz-auth。</p>
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("角色查询控制器")
class RoleQueryControllerTest {

    @Mock
    private RoleDomain roleDomain;

    @InjectMocks
    private RoleQueryController roleQueryController;

    @Test
    @DisplayName("角色列表: 透传查询条件")
    void roleListShouldDelegate() {
        RoleQuery query = new RoleQuery();
        when(roleDomain.list(query)).thenReturn(List.of(new RoleRes()));

        assertEquals(1, roleQueryController.roleList(query).getData().size());
    }

    @Test
    @DisplayName("角色详情: 透传角色 ID")
    void roleDetailShouldPassId() {
        RoleRes res = new RoleRes();
        res.setName("渠道商");
        when(roleDomain.detail(9L)).thenReturn(res);

        assertEquals("渠道商", roleQueryController.roleDetail(9L).getData().getName());
    }

    @Test
    @DisplayName("角色保存: 请求体带 id 走修改")
    void roleListSaveShouldEditWhenIdPresent() {
        RoleReq req = new RoleReq();
        req.setId(5L);

        roleQueryController.roleListSave(req);

        verify(roleDomain).edit(5L, req);
        verify(roleDomain, never()).save(any());
    }

    @Test
    @DisplayName("角色保存: 请求体无 id 走新建")
    void roleListSaveShouldSaveWhenIdAbsent() {
        RoleReq req = new RoleReq();

        roleQueryController.roleListSave(req);

        verify(roleDomain).save(req);
        verify(roleDomain, never()).edit(any(), any());
    }
}
