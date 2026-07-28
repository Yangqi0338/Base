package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.IdentityService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.RoleDomain;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkQuery;
import com.newzkl.platform.base.biz.account.model.cdk.req.ToCdkCommand;
import com.newzkl.platform.base.biz.account.model.req.RoleApplyCommand;
import com.newzkl.platform.base.biz.account.model.req.RoleQuery;
import com.newzkl.platform.base.biz.account.model.role.req.RoleReq;
import com.newzkl.platform.base.biz.account.model.role.res.RoleRes;
import com.newzkl.platform.base.biz.account.model.vo.PromiseFlowVO;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link RoleController} 入参与出参契约测试。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("角色控制器")
class RoleControllerTest {

    private static final Long ACCOUNT_ID = 7701L;

    @Mock
    private RoleDomain roleDomain;

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private IdentityService identityService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUpLoginState() {
        SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, ACCOUNT_ID.toString());
    }

    @AfterEach
    void clearLoginState() {
        SecurityContextHolder.remove();
    }

    /**
     * 设置登录态角色。
     *
     * @param role 角色
     */
    private void loginAs(RoleEnum.CompanyRole role) {
        SecurityContextHolder.set(TokenConstants.ROLE, role.getCodeStr());
    }

    @Test
    @DisplayName("申请角色: 透传申请资料并返回审批流 ID")
    void applyRoleShouldDelegate() {
        RoleApplyCommand command = new RoleApplyCommand();
        when(identityService.applyRole(command)).thenReturn(66L);

        assertEquals(66L, roleController.applyRole(command).getData());
    }

    @Test
    @DisplayName("保存申请资料: 透传申请资料")
    void saveApplyCommandShouldDelegate() {
        RoleApplyCommand command = new RoleApplyCommand();

        roleController.saveApplyCommand(command);

        verify(identityService).saveApplyCommand(command);
    }

    @Test
    @DisplayName("加载申请资料: 保留旧语义 —— 异常吞掉并返回 null")
    void loadApplyCommandShouldSwallowException() {
        when(identityService.loadApplyCommand(1001L)).thenThrow(new RuntimeException("redis down"));

        assertNull(roleController.loadApplyCommand(1001L).getData());
    }

    @Test
    @DisplayName("加载申请资料: 正常路径透传角色 ID")
    void loadApplyCommandShouldPassRoleId() {
        RoleApplyCommand command = new RoleApplyCommand();
        when(identityService.loadApplyCommand(1001L)).thenReturn(command);

        assertEquals(command, roleController.loadApplyCommand(1001L).getData());
    }

    @Test
    @DisplayName("保证金: 透传流水并返回审批流 ID")
    void submitPromiseFlowShouldDelegate() {
        PromiseFlowVO flow = new PromiseFlowVO();
        when(identityService.submitPromiseFlow(flow)).thenReturn(88L);

        assertEquals(88L, roleController.submitPromiseFlow(flow).getData());
    }

    @Test
    @DisplayName("角色列表: 透传查询条件")
    void roleListShouldDelegate() {
        RoleQuery query = new RoleQuery();
        when(roleDomain.list(query)).thenReturn(List.of(new RoleRes()));

        assertEquals(1, roleController.roleList(query).getData().size());
    }

    @Test
    @DisplayName("角色详情: 透传角色 ID")
    void roleDetailShouldPassId() {
        RoleRes res = new RoleRes();
        res.setName("渠道商");
        when(roleDomain.detail(9L)).thenReturn(res);

        assertEquals("渠道商", roleController.roleDetail(9L).getData().getName());
    }

    @Test
    @DisplayName("角色保存: 请求体带 id 走修改")
    void roleListSaveShouldEditWhenIdPresent() {
        RoleReq req = new RoleReq();
        req.setId(5L);

        roleController.roleListSave(req);

        verify(roleDomain).edit(5L, req);
        verify(roleDomain, never()).save(any());
    }

    @Test
    @DisplayName("角色保存: 请求体无 id 走新建")
    void roleListSaveShouldSaveWhenIdAbsent() {
        RoleReq req = new RoleReq();

        roleController.roleListSave(req);

        verify(roleDomain).save(req);
        verify(roleDomain, never()).edit(any(), any());
    }

    @Test
    @DisplayName("开通码列表: 平台角色不注入数据范围")
    void cdkListPlatformShouldNotScope() {
        loginAs(RoleEnum.CompanyRole.PLATFORM);
        when(userQueryService.cdkPage(any())).thenReturn(new Page<>());
        CdkQuery query = new CdkQuery();

        roleController.cdkList(query);

        assertNull(query.getOperatorId());
        assertNull(query.getDealerId());
        assertNull(query.getChannelId());
    }

    @Test
    @DisplayName("开通码列表: 运营商注入 operatorId")
    void cdkListOperatorShouldScopeByOperator() {
        loginAs(RoleEnum.CompanyRole.OPERATOR);
        when(userQueryService.cdkPage(any())).thenReturn(new Page<>());
        CdkQuery query = new CdkQuery();

        roleController.cdkList(query);

        assertEquals(ACCOUNT_ID, query.getOperatorId());
    }

    @Test
    @DisplayName("开通码列表: 交易师注入 dealerId")
    void cdkListDealerShouldScopeByDealer() {
        loginAs(RoleEnum.CompanyRole.DEALER);
        when(userQueryService.cdkPage(any())).thenReturn(new Page<>());
        CdkQuery query = new CdkQuery();

        roleController.cdkList(query);

        assertEquals(ACCOUNT_ID, query.getDealerId());
    }

    @Test
    @DisplayName("开通码列表: 渠道商注入 channelId")
    void cdkListChannelShouldScopeByChannel() {
        loginAs(RoleEnum.CompanyRole.CHANNEL);
        when(userQueryService.cdkPage(any())).thenReturn(new Page<>());
        CdkQuery query = new CdkQuery();

        roleController.cdkList(query);

        assertEquals(ACCOUNT_ID, query.getChannelId());
    }

    @Test
    @DisplayName("分配开通码: 分配人角色与账号取自登录态")
    void toCdkShouldFillFromLoginState() {
        loginAs(RoleEnum.CompanyRole.OPERATOR);
        ToCdkCommand command = new ToCdkCommand();
        command.setToRole(RoleEnum.CompanyRole.DEALER.getCode());
        command.setToUserId(8L);
        command.setCdkIdList(List.of(1L));

        roleController.toCdk(command);

        assertEquals(RoleEnum.CompanyRole.OPERATOR.getCode(), command.getFromRole());
        assertEquals(ACCOUNT_ID, command.getFromUserId());
        verify(identityService).toCdk(command);
    }
}
