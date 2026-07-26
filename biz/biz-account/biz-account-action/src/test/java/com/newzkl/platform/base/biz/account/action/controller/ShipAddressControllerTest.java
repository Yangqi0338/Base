package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.service.ShipAddressDomain;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressQuery;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressReq;
import com.newzkl.platform.base.biz.account.model.address.res.ShipAddressRes;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link ShipAddressController} 入参组装测试。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("收货地址控制器")
class ShipAddressControllerTest {

    private static final Long ACCOUNT_ID = 5501L;

    @Mock
    private ShipAddressDomain shipAddressDomain;

    @InjectMocks
    private ShipAddressController shipAddressController;

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
    @DisplayName("新建: 透传领域返回的主键")
    void shipAddressSaveShouldReturnId() {
        when(shipAddressDomain.save(any())).thenReturn(90001L);

        ScmResult<Long> result = shipAddressController.shipAddressSave(new ShipAddressReq());

        assertEquals(90001L, result.getData());
    }

    @Test
    @DisplayName("修改: 以请求体 id 作为更新目标")
    void shipAddressEditShouldPassBodyId() {
        ShipAddressReq req = new ShipAddressReq();
        req.setId(111L);

        shipAddressController.shipAddressEdit(req);

        verify(shipAddressDomain).edit(111L, req);
    }

    @Test
    @DisplayName("删除: 透传 ID 列表")
    void shipAddressDeleteShouldPassIdList() {
        IdListCommand command = new IdListCommand();
        command.setIdList(List.of(1L, 2L));

        shipAddressController.shipAddressDelete(command);

        verify(shipAddressDomain).delete(List.of(1L, 2L));
    }

    @Test
    @DisplayName("分页: 按登录态回填账号 ID")
    void shipAddressPageShouldFillAccountId() {
        when(shipAddressDomain.pageList(any())).thenReturn(new Page<>());

        shipAddressController.shipAddressPage(new ShipAddressQuery());

        ArgumentCaptor<ShipAddressQuery> captor = ArgumentCaptor.forClass(ShipAddressQuery.class);
        verify(shipAddressDomain).pageList(captor.capture());
        assertEquals(ACCOUNT_ID, captor.getValue().getAccountId());
    }

    @Test
    @DisplayName("详情: 透传路径 ID 并原样返回结果")
    void shipAddressDetailShouldPassId() {
        ShipAddressRes res = new ShipAddressRes();
        res.setId(333L);
        when(shipAddressDomain.detail(anyLong())).thenReturn(res);

        assertEquals(333L, shipAddressController.shipAddress(333L).getData().getId());
        verify(shipAddressDomain).detail(333L);
    }

    @Test
    @DisplayName("默认地址: 委托领域层按登录态查询")
    void defaultShipAddressShouldDelegate() {
        when(shipAddressDomain.defaultShipAddress()).thenReturn(null);

        shipAddressController.defaultShipAddress();

        verify(shipAddressDomain).defaultShipAddress();
    }
}
