package com.newzkl.platform.base.biz.account.domain.service.impl;

import com.newzkl.platform.base.biz.account.domain.repository.ShipAddressRepository;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressQuery;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressReq;
import com.newzkl.platform.base.biz.account.model.address.res.ShipAddressRes;
import com.newzkl.platform.base.biz.account.model.address.vo.ShipAddressVO;
import com.newzkl.platform.base.biz.account.model.assembler.ShipAddressAssembler;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
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

import java.util.List;

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
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("收货地址领域服务")
class ShipAddressDomainImplTest {

    private static final Long ACCOUNT_ID = 5501L;
    private static final Long ROLE_ID = 1002L;
    private static final Long NEW_ID = 90001L;

    @Mock
    private ShipAddressRepository shipAddressRepository;

    private ShipAddressDomainImpl shipAddressDomain;

    /**
     * 以真实转换语义替代 MapStruct 生成实现, 避免测试依赖注解处理器产物
     */
    private final ShipAddressAssembler assembler = new ShipAddressAssembler() {
        @Override
        public ShipAddressVO req2VO(ShipAddressReq entity) {
            return TransferUtils.transfer(entity, ShipAddressVO::new);
        }

        @Override
        public List<ShipAddressVO> req2VO(List<ShipAddressReq> entity) {
            return TransferUtils.transfers(entity, ShipAddressVO::new);
        }

        @Override
        public ShipAddressRes vo2Res(ShipAddressVO it) {
            return TransferUtils.transfer(it, ShipAddressRes::new);
        }
    };

    @BeforeEach
    void setUp() {
        shipAddressDomain = new ShipAddressDomainImpl(shipAddressRepository, assembler);
        SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, ACCOUNT_ID.toString());
        SecurityContextHolder.set(TokenConstants.DETAILS_IDENTITY, ROLE_ID.toString());
    }

    @AfterEach
    void clearLoginState() {
        SecurityContextHolder.remove();
    }

    @Test
    @DisplayName("修改: 以入参 id 为准而非请求体 id, 默认地址先置其他为非默认")
    void editShouldUseGivenIdAndResetOtherDefault() {
        when(shipAddressRepository.edit(any())).thenReturn(1);
        ShipAddressReq req = new ShipAddressReq();
        req.setId(111L);
        req.setIsDefault(1);

        int rows = shipAddressDomain.edit(222L, req);

        assertEquals(1, rows);
        ArgumentCaptor<ShipAddressVO> captor = ArgumentCaptor.forClass(ShipAddressVO.class);
        verify(shipAddressRepository).edit(captor.capture());
        assertEquals(222L, captor.getValue().getId(), "更新目标应为入参 id");
        verify(shipAddressRepository).setOtherNotDefault(AccountEnum.Identity.CHANNEL, ACCOUNT_ID, 222L);
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
