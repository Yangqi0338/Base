package com.newzkl.platform.base.biz.order.domain.service.impl;

import com.newzkl.platform.base.biz.order.domain.adapt.repository.ShipAddressRepository;
import com.newzkl.platform.base.biz.order.model.req.query.ShipAddressQuery;
import com.newzkl.platform.base.biz.order.model.req.ShipAddressReq;
import com.newzkl.platform.base.biz.order.model.res.ShipAddressRes;
import com.newzkl.platform.base.biz.order.model.vo.ShipAddressVO;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code ShipAddressDomainImpl} 行为测试
 *
 * <p>纯 JUnit 5 + Mockito, 不启动 Spring 容器, 不连数据库。
 * 登录态通过 {@code SecurityContextHolder} 直接种入。</p>
 *
 * <p>并入订单域后领域服务不再持有 MapStruct assembler, 改由 {@code TransferUtils} 同名字段拷贝,
 * 故测试直接以仓储 mock 构造实现</p>
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("收货地址领域服务")
class ShipAddressDomainImplTest {

    private static final Long ACCOUNT_ID = 5501L;
    private static final Long ROLE_ID = 1002L;
    private static final Long NEW_ID = 90001L;
    private static final Long EXIST_ID = 111L;

    @Mock
    private ShipAddressRepository shipAddressRepository;

    private ShipAddressDomainImpl shipAddressDomain;

    @BeforeEach
    void setUp() {
        shipAddressDomain = new ShipAddressDomainImpl(shipAddressRepository);
        SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, ACCOUNT_ID.toString());
        SecurityContextHolder.set(TokenConstants.DETAILS_IDENTITY, ROLE_ID.toString());
    }

    @AfterEach
    void clearLoginState() {
        SecurityContextHolder.remove();
    }

    @Test
    @DisplayName("修改: 请求体带 id 走 edit 而非 save, 默认地址置其他为非默认")
    void editShouldKeepRequestIdAndResetOtherDefault() {
        when(shipAddressRepository.edit(any())).thenReturn(1);
        ShipAddressReq req = new ShipAddressReq();
        req.setId(EXIST_ID);
        req.setIsDefault(1);
        req.setIdentity(AccountEnum.Identity.CHANNEL);
        req.setAccountId(ACCOUNT_ID);

        assertEquals(EXIST_ID, shipAddressDomain.save(req), "修改应沿用请求体 id");

        ArgumentCaptor<ShipAddressVO> captor = ArgumentCaptor.forClass(ShipAddressVO.class);
        verify(shipAddressRepository).edit(captor.capture());
        assertEquals(EXIST_ID, captor.getValue().getId(), "更新目标应为请求体 id");
        verify(shipAddressRepository, never()).save(any());
        verify(shipAddressRepository).setOtherNotDefault(AccountEnum.Identity.CHANNEL, ACCOUNT_ID, EXIST_ID);
    }

    @Test
    @DisplayName("新增: 请求体无 id 走 save 取自增 id, 非默认地址不动其他记录")
    void saveShouldUseGeneratedIdAndKeepOtherDefault() {
        when(shipAddressRepository.save(any())).thenReturn(NEW_ID);
        ShipAddressReq req = new ShipAddressReq();
        req.setIsDefault(0);
        req.setIdentity(AccountEnum.Identity.CHANNEL);
        req.setAccountId(ACCOUNT_ID);

        assertEquals(NEW_ID, shipAddressDomain.save(req), "新增应返回仓储生成 id");

        verify(shipAddressRepository, never()).edit(any());
        verify(shipAddressRepository, never()).setOtherNotDefault(any(), anyLong(), anyLong());
    }

    @Test
    @DisplayName("默认地址查询: 按登录态组装角色/账号并限定默认标记")
    void defaultShipAddressShouldQueryByLoginState() {
        ShipAddressVO vo = new ShipAddressVO();
        vo.setId(333L);
        when(shipAddressRepository.findByQuery(any())).thenReturn(vo);

        ShipAddressRes res = shipAddressDomain.defaultShipAddress();

        assertEquals(333L, res.getId());
        ArgumentCaptor<ShipAddressQuery> captor = ArgumentCaptor.forClass(ShipAddressQuery.class);
        verify(shipAddressRepository).findByQuery(captor.capture());
        assertEquals(AccountEnum.Identity.CHANNEL, captor.getValue().getIdentity());
        assertEquals(ACCOUNT_ID, captor.getValue().getAccountId());
        assertEquals(1, captor.getValue().getIsDefault());
    }

    @Test
    @DisplayName("详情: 记录不存在时返回 null 而不抛异常")
    void detailShouldReturnNullWhenAbsent() {
        when(shipAddressRepository.detail(999L)).thenReturn(null);

        assertNull(shipAddressDomain.detail(999L));
    }
}
