package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.action.cmd.MerchantCmd;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.MerchantDomain;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantQuery;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantReq;
import com.newzkl.platform.base.biz.account.model.merchant.res.MerchantRes;
import com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link MerchantController} 入参与出参契约测试。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("商户控制器")
class MerchantControllerTest {

    private static final Long ACCOUNT_ID = 6601L;

    @Mock
    private MerchantDomain merchantDomain;

    @Mock
    private UserQueryService userQueryService;

    @InjectMocks
    private MerchantController merchantController;

    @BeforeEach
    void setUpLoginState() {
        SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, ACCOUNT_ID.toString());
    }

    @AfterEach
    void clearLoginState() {
        SecurityContextHolder.remove();
    }

    @Test
    @DisplayName("修改: 以命令体 id 为更新目标, 透传商户字段")
    void merchantEditShouldPassCommandId() {
        MerchantCmd.Edit edit = new MerchantCmd.Edit();
        edit.setId(222L);
        MerchantReq req = new MerchantReq();
        edit.setMerchantCommand(req);

        merchantController.merchantEdit(edit);

        verify(merchantDomain).edit(222L, req);
    }

    @Test
    @DisplayName("删除: 透传旧字段名 merchantIdList")
    void merchantDeleteShouldPassIdList() {
        MerchantCmd.IDList command = new MerchantCmd.IDList();
        command.setMerchantIdList(List.of(1L, 2L));

        merchantController.merchantDelete(command);

        verify(merchantDomain).delete(List.of(1L, 2L));
    }

    @Test
    @DisplayName("详情: 按登录态账号 ID 查询")
    void merchantShouldQueryByLoginAccount() {
        MerchantRes res = new MerchantRes();
        res.setId(ACCOUNT_ID);
        when(userQueryService.merchantVO(anyLong())).thenReturn(res);

        assertEquals(ACCOUNT_ID, merchantController.merchant().getData().getId());
        verify(userQueryService).merchantVO(ACCOUNT_ID);
    }

    @Test
    @DisplayName("分页: 透传查询条件与分页结果")
    void merchantPageShouldDelegate() {
        when(userQueryService.merchantPage(any())).thenReturn(new Page<>());
        MerchantQuery query = new MerchantQuery();

        PlatformResult<Page<MerchantRes>> result = merchantController.merchantPage(query);

        assertEquals(0, result.getData().getRecords().size());
        verify(userQueryService).merchantPage(query);
    }

    @Test
    @DisplayName("使用开通码: 只透传开通码值, 使用者由领域层取登录态")
    void useStoreCdkShouldPassValueOnly() {
        MerchantCmd.Cdk cdk = new MerchantCmd.Cdk();
        cdk.setCdk("abc123");

        merchantController.useStoreCdk(cdk);

        verify(merchantDomain).useStoreCdk("abc123");
    }

    @Test
    @DisplayName("appId: 取配置中的 appId")
    void appIdShouldReturnAppId() {
        WxMpConfigVO config = new WxMpConfigVO();
        config.setAppId("wx123");
        when(userQueryService.wxMpConfig(7L)).thenReturn(config);
        MerchantCmd.ID idObj = new MerchantCmd.ID();
        idObj.setId(7L);

        assertEquals("wx123", merchantController.appId(idObj).getData());
    }

    @Test
    @DisplayName("appId: 未配置微信参数时返回 null 而不抛空指针")
    void appIdShouldTolerateAbsentConfig() {
        when(userQueryService.wxMpConfig(7L)).thenReturn(null);
        MerchantCmd.ID idObj = new MerchantCmd.ID();
        idObj.setId(7L);

        assertNull(merchantController.appId(idObj).getData());
    }

    @Test
    @DisplayName("微信参数查询: 透传商户 ID")
    void getWxMpConfigShouldPassId() {
        when(userQueryService.wxMpConfig(7L)).thenReturn(new WxMpConfigVO());
        MerchantCmd.ID idObj = new MerchantCmd.ID();
        idObj.setId(7L);

        merchantController.getWxMpConfigVO(idObj);

        verify(userQueryService).wxMpConfig(7L);
    }

    @Test
    @DisplayName("微信参数配置: 目标商户由领域层取登录态")
    void setWxMpConfigShouldDelegate() {
        WxMpConfigVO config = new WxMpConfigVO();
        MerchantCmd.EditWxMpConfigVO cmd = new MerchantCmd.EditWxMpConfigVO();
        cmd.setWxMpConfigVO(config);

        merchantController.setWxMpConfigVO(cmd);

        verify(merchantDomain).wxMpConfigSet(config);
    }
}
