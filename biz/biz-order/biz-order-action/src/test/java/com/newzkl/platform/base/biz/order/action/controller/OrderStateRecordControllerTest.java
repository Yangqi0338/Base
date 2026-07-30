package com.newzkl.platform.base.biz.order.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.OrderStateRecordService;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateRecordVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code OrderStateRecordController} 入参组装测试
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("订单状态记录控制器")
class OrderStateRecordControllerTest {

    @Mock
    private OrderStateRecordService orderStateRecordService;

    @InjectMocks
    private OrderStateRecordController orderStateRecordController;

    @Test
    @DisplayName("分页: 透传分页入参并返回 MP 分页壳")
    void pageQueryShouldReturnPage() {
        OrderStateRecordPageReq req = new OrderStateRecordPageReq();
        Page<OrderStateRecordVO> page = new Page<>(1, 10);
        page.setTotal(3);
        when(orderStateRecordService.pageQuery(req)).thenReturn(page);

        PlatformResult<Page<OrderStateRecordVO>> result = orderStateRecordController.pageQuery(req);

        assertNotNull(result.getData());
        assertEquals(3, result.getData().getTotal());
        assertTrue(result.isSuccess());
        verify(orderStateRecordService).pageQuery(req);
    }
}
