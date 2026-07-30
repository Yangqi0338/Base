package com.newzkl.platform.base.biz.finance.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.virtual.service.VirtualAssetsDomain;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsRecordQuery;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code VirtualAssetsController} 登录态补齐测试
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class VirtualAssetsControllerTest {

    private static final Long ACCOUNT_ID = 5501L;

    @Mock
    private VirtualAssetsDomain virtualAssetsDomain;

    @InjectMocks
    private VirtualAssetsController virtualAssetsController;

    @BeforeEach
    void setUpLoginState() {
        SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, ACCOUNT_ID.toString());
        SecurityContextHolder.set(TokenConstants.ROLE, "1002");
    }

    @AfterEach
    void clearLoginState() {
        SecurityContextHolder.remove();
    }

    @Test
    @DisplayName("资产查询: 入参缺省时按登录态补齐账户 id 与账户类型")
    void queryVirtualAssetsShouldFillCurrentLoginState() {
        when(virtualAssetsDomain.queryVirtualAssets(any())).thenReturn(List.of());

        virtualAssetsController.queryVirtualAssets(new VirtualAssetsQuery());

        ArgumentCaptor<VirtualAssetsQuery> captor = ArgumentCaptor.forClass(VirtualAssetsQuery.class);
        verify(virtualAssetsDomain).queryVirtualAssets(captor.capture());
        assertEquals(ACCOUNT_ID, captor.getValue().getAccountId());
        assertEquals(PurseEnum.FinanceUser.CHANNEL, captor.getValue().getAccountType());
    }

    @Test
    @DisplayName("资产查询: 入参已带账户信息时不被登录态覆盖")
    void queryVirtualAssetsShouldKeepExplicitAccount() {
        when(virtualAssetsDomain.queryVirtualAssets(any())).thenReturn(List.of());
        VirtualAssetsQuery query = new VirtualAssetsQuery();
        query.setAccountId(6602L);
        query.setAccountType(PurseEnum.FinanceUser.SUPPLIER);

        virtualAssetsController.queryVirtualAssets(query);

        ArgumentCaptor<VirtualAssetsQuery> captor = ArgumentCaptor.forClass(VirtualAssetsQuery.class);
        verify(virtualAssetsDomain).queryVirtualAssets(captor.capture());
        assertEquals(6602L, captor.getValue().getAccountId());
        assertEquals(PurseEnum.FinanceUser.SUPPLIER, captor.getValue().getAccountType());
    }

    @Test
    @DisplayName("变动记录查询: 入参缺省时按登录态补齐账户 id 与账户类型")
    void queryVirtualAssetsRecordShouldFillCurrentLoginState() {
        when(virtualAssetsDomain.queryVirtualAssetsRecord(any())).thenReturn(new Page<>());

        virtualAssetsController.queryVirtualAssetsRecord(new VirtualAssetsRecordQuery());

        ArgumentCaptor<VirtualAssetsRecordQuery> captor = ArgumentCaptor.forClass(VirtualAssetsRecordQuery.class);
        verify(virtualAssetsDomain).queryVirtualAssetsRecord(captor.capture());
        assertEquals(ACCOUNT_ID, captor.getValue().getAccountId());
        assertEquals(PurseEnum.FinanceUser.CHANNEL, captor.getValue().getAccountType());
    }
}
