package com.newzkl.platform.base.biz.order.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.action.cmd.OrderCmd;
import com.newzkl.platform.base.biz.order.application.service.RefundOperationRecordService;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundOperationRecordVO;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code RefundOperationRecordController} 端点透传测试
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("售后协商记录控制器")
class RefundOperationRecordControllerTest {

    @Mock
    private RefundOperationRecordService refundOperationRecordService;

    @InjectMocks
    private RefundOperationRecordController refundOperationRecordController;

    @BeforeEach
    void setUpLoginState() {
        SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, "6001");
        SecurityContextHolder.set(TokenConstants.ROLE, "1002");
    }

    @AfterEach
    void clearLoginState() {
        SecurityContextHolder.remove();
    }

    @Test
    @DisplayName("新增: 透传应用层返回的 VO")
    void createShouldReturnVO() {
        RefundOperationRecordVO vo = new RefundOperationRecordVO();
        vo.setId(1L);
        when(refundOperationRecordService.create(any())).thenReturn(vo);

        PlatformResult<RefundOperationRecordVO> result =
                refundOperationRecordController.create(new RefundOperationRecordCreateReq());

        assertEquals(1L, result.getData().getId());
    }

    @Test
    @DisplayName("修改: 透传入参给应用层")
    void updateShouldDelegate() {
        RefundOperationRecordUpdateReq req = new RefundOperationRecordUpdateReq();
        req.setId(2L);
        when(refundOperationRecordService.update(req)).thenReturn(new RefundOperationRecordVO());

        refundOperationRecordController.update(req);

        verify(refundOperationRecordService).update(req);
    }

    @Test
    @DisplayName("删除: 取命令体的 id")
    void deleteShouldPassCommandId() {
        OrderCmd.ID idObj = new OrderCmd.ID();
        idObj.setId(3L);

        PlatformResult<Void> result = refundOperationRecordController.delete(idObj);

        verify(refundOperationRecordService).delete(3L);
        assertTrue(result.getSuccess());
    }

    @Test
    @DisplayName("按售后单查询: 透传 refundId")
    void listByRefundIdShouldPassId() {
        when(refundOperationRecordService.listByRefundId(anyLong())).thenReturn(List.of());

        assertEquals(0, refundOperationRecordController.listByRefundId(5L).getData().size());
        verify(refundOperationRecordService).listByRefundId(5L);
    }

    @Test
    @DisplayName("分页: 返回 MyBatis-Plus Page")
    void pageQueryShouldReturnPage() {
        Page<RefundOperationRecordVO> page = new Page<>(1, 10);
        page.setTotal(7);
        RefundOperationRecordPageReq req = new RefundOperationRecordPageReq();
        when(refundOperationRecordService.pageQuery(req)).thenReturn(page);

        assertEquals(7, refundOperationRecordController.pageQuery(req).getData().getTotal());
    }
}
