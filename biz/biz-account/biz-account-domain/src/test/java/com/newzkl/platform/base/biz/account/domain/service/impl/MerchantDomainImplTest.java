package com.newzkl.platform.base.biz.account.domain.service.impl;

import com.newzkl.platform.base.biz.account.domain.repository.CdkRepository;
import com.newzkl.platform.base.biz.account.domain.repository.MerchantRepository;
import com.newzkl.platform.base.biz.account.model.assembler.MerchantAssembler;
import com.newzkl.platform.base.biz.account.model.cdk.vo.CdkVO;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantReq;
import com.newzkl.platform.base.biz.account.model.merchant.res.MerchantRes;
import com.newzkl.platform.base.biz.account.model.merchant.vo.MerchantVO;
import com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link MerchantDomainImpl} 行为测试。
 *
 * <p>纯 JUnit 5 + Mockito, 不启动 Spring 容器, 不连数据库。登录态直接种入
 * {@link SecurityContextHolder}。</p>
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("商户领域服务")
class MerchantDomainImplTest {

    private static final Long ACCOUNT_ID = 6601L;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CdkRepository cdkRepository;

    private MerchantDomainImpl merchantDomain;

    /**
     * 以真实转换语义替代 MapStruct 生成实现, 避免测试依赖注解处理器产物。
     */
    private final MerchantAssembler assembler = new MerchantAssembler() {
        @Override
        public MerchantVO req2VO(MerchantReq entity) {
            return TransferUtils.transfer(entity, MerchantVO::new);
        }

        @Override
        public List<MerchantVO> req2VO(List<MerchantReq> entity) {
            return TransferUtils.transfers(entity, MerchantVO::new);
        }

        @Override
        public MerchantRes vo2Res(MerchantVO it) {
            return TransferUtils.transfer(it, MerchantRes::new);
        }
    };

    @BeforeEach
    void setUp() {
        merchantDomain = new MerchantDomainImpl(merchantRepository, cdkRepository, assembler);
        SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, ACCOUNT_ID.toString());
        SecurityContextHolder.set(TokenConstants.DETAILS_USERNAME, "13000000000");
    }

    @AfterEach
    void clearLoginState() {
        SecurityContextHolder.remove();
    }

    @Test
    @DisplayName("修改: 以入参 id 为更新目标, 而非请求体 id")
    void editShouldUseGivenId() {
        when(merchantRepository.edit(any())).thenReturn(1);
        MerchantReq req = new MerchantReq();
        req.setId(111L);
        req.setName("门店A");

        int rows = merchantDomain.edit(222L, req);

        assertEquals(1, rows);
        ArgumentCaptor<MerchantVO> captor = ArgumentCaptor.forClass(MerchantVO.class);
        verify(merchantRepository).edit(captor.capture());
        assertEquals(222L, captor.getValue().getId());
        assertEquals("门店A", captor.getValue().getName());
    }

    @Test
    @DisplayName("当前商户详情: 按登录态账号 ID 查询")
    void currentMerchantShouldUseLoginAccount() {
        MerchantVO vo = new MerchantVO();
        vo.setId(ACCOUNT_ID);
        vo.setName("门店A");
        when(merchantRepository.detail(ACCOUNT_ID)).thenReturn(vo);

        MerchantRes res = merchantDomain.currentMerchant();

        assertEquals(ACCOUNT_ID, res.getId());
        assertEquals("门店A", res.getName());
    }

    @Test
    @DisplayName("详情: 记录不存在时返回 null 而不抛异常")
    void detailShouldReturnNullWhenAbsent() {
        when(merchantRepository.detail(999L)).thenReturn(null);

        assertNull(merchantDomain.detail(999L));
    }

    @Test
    @DisplayName("自助注册: 主键取账号 ID, 名称为空回落 username, 门店权限初始为 0")
    void customSaveShouldKeepLegacyDefaults() {
        MerchantCustomSaveReq req = new MerchantCustomSaveReq();
        req.setAccountId(777L);
        req.setUsername("13000000000");

        merchantDomain.customSave(req);

        ArgumentCaptor<MerchantVO> captor = ArgumentCaptor.forClass(MerchantVO.class);
        verify(merchantRepository).save(captor.capture());
        assertEquals(777L, captor.getValue().getId());
        assertEquals("13000000000", captor.getValue().getName(), "名称为空应回落为 username");
        assertEquals(0, captor.getValue().getStorePermission());
    }

    @Test
    @DisplayName("自助注册: 名称非空时保留原名称")
    void customSaveShouldKeepGivenName() {
        MerchantCustomSaveReq req = new MerchantCustomSaveReq();
        req.setAccountId(777L);
        req.setUsername("13000000000");
        req.setName("门店A");

        merchantDomain.customSave(req);

        ArgumentCaptor<MerchantVO> captor = ArgumentCaptor.forClass(MerchantVO.class);
        verify(merchantRepository).save(captor.capture());
        assertEquals("门店A", captor.getValue().getName());
    }

    @Test
    @DisplayName("使用开通码: 占用开通码并开通登录商户的门店权限")
    void useStoreCdkShouldConsumeCdkAndGrantPermission() {
        CdkVO cdk = new CdkVO();
        cdk.setId(8801L);
        cdk.setValue("abc123");
        cdk.setUseState(0);
        when(cdkRepository.idByValue("abc123")).thenReturn(8801L);
        when(cdkRepository.detail(8801L)).thenReturn(cdk);

        merchantDomain.useStoreCdk("abc123");

        ArgumentCaptor<CdkVO> cdkCaptor = ArgumentCaptor.forClass(CdkVO.class);
        verify(cdkRepository).edit(cdkCaptor.capture());
        assertEquals(1, cdkCaptor.getValue().getUseState());
        assertEquals(ACCOUNT_ID, cdkCaptor.getValue().getUseId());
        assertTrue(cdkCaptor.getValue().getUseTime() != null, "兑换时间应被写入");

        ArgumentCaptor<MerchantVO> merchantCaptor = ArgumentCaptor.forClass(MerchantVO.class);
        verify(merchantRepository).edit(merchantCaptor.capture());
        assertEquals(ACCOUNT_ID, merchantCaptor.getValue().getId());
        assertEquals(1, merchantCaptor.getValue().getStorePermission());
    }

    @Test
    @DisplayName("使用开通码: 已使用则抛参数异常且不改商户")
    void useStoreCdkShouldRejectUsedCdk() {
        CdkVO cdk = new CdkVO();
        cdk.setId(8801L);
        cdk.setUseState(1);
        when(cdkRepository.idByValue("abc123")).thenReturn(8801L);
        when(cdkRepository.detail(8801L)).thenReturn(cdk);

        PlatformException ex = assertThrows(PlatformException.class, () -> merchantDomain.useStoreCdk("abc123"));

        assertTrue(ex.equalsCode(BaseErrorCode.PARAM));
        verify(cdkRepository, never()).edit(any());
        verify(merchantRepository, never()).edit(any());
    }

    @Test
    @DisplayName("使用开通码: 开通码不存在时抛记录不存在")
    void useStoreCdkShouldRejectAbsentCdk() {
        when(cdkRepository.idByValue("nope")).thenReturn(null);
        when(cdkRepository.detail(null)).thenReturn(null);

        PlatformException ex = assertThrows(PlatformException.class, () -> merchantDomain.useStoreCdk("nope"));

        assertTrue(ex.equalsCode(BaseErrorCode.NODATA));
        verify(merchantRepository, never()).edit(any());
    }

    @Test
    @DisplayName("微信配置写入: 仅带登录商户 ID 与配置列")
    void wxMpConfigSetShouldOnlyTouchConfigColumn() {
        WxMpConfigVO config = new WxMpConfigVO();
        config.setAppId("wx123");

        merchantDomain.wxMpConfigSet(config);

        ArgumentCaptor<MerchantVO> captor = ArgumentCaptor.forClass(MerchantVO.class);
        verify(merchantRepository, times(1)).edit(captor.capture());
        MerchantVO edited = captor.getValue();
        assertEquals(ACCOUNT_ID, edited.getId());
        assertEquals("wx123", edited.getWxMpConfig().getAppId());
        assertNull(edited.getName(), "不应连带更新其他列");
        assertNull(edited.getStorePermission(), "不应连带更新门店权限");
    }
}
