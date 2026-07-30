package com.newzkl.platform.base.biz.order.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.service.RefundOperationRecordDomain;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.biz.order.model.order.dto.RefundOperationRecord;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundOperationRecordVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code RefundOperationRecordServiceImpl} 编排测试
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("售后操作记录应用服务")
class RefundOperationRecordServiceImplTest {

    @Mock
    private RefundOperationRecordDomain refundOperationRecordDomain;

    @InjectMocks
    private RefundOperationRecordServiceImpl refundOperationRecordService;

    @Test
    @DisplayName("新增: 状态 code 转枚举后委托领域层")
    void createShouldMapStateCodeToEnum() {
        RefundOperationRecordCreateReq req = new RefundOperationRecordCreateReq();
        req.setSpuOrderNo("SPU1");
        req.setRefundId(11L);
        req.setBeforeState(RefundEnum.State.CHANNEL_WAIT.getCode());
        req.setAfterState(RefundEnum.State.SUPPLIER_WAIT.getCode());
        req.setOperationContent("审核通过");
        when(refundOperationRecordDomain.create(any())).thenAnswer(it -> it.getArgument(0));

        RefundOperationRecordVO vo = refundOperationRecordService.create(req);

        ArgumentCaptor<RefundOperationRecord> captor = ArgumentCaptor.forClass(RefundOperationRecord.class);
        verify(refundOperationRecordDomain).create(captor.capture());
        assertEquals(RefundEnum.State.CHANNEL_WAIT, captor.getValue().getBeforeState());
        assertEquals(RefundEnum.State.SUPPLIER_WAIT, captor.getValue().getAfterState());
        assertEquals("SPU1", vo.getSpuOrderNo());
        assertEquals(RefundEnum.State.SUPPLIER_WAIT, vo.getAfterState());
    }

    @Test
    @DisplayName("修改: 仅覆盖描述性字段, 业务主数据由原记录回填")
    void updateShouldOnlyOverrideDescriptiveFields() {
        RefundOperationRecord old = new RefundOperationRecord();
        old.setId(5L);
        old.setRefundId(99L);
        old.setSpuOrderNo("SPU9");
        old.setOperatorRoleCode(1001L);
        old.setAfterState(RefundEnum.State.RECEIVE_WAIT);
        old.setOperationContent("旧内容");
        when(refundOperationRecordDomain.findById(5L)).thenReturn(old);
        when(refundOperationRecordDomain.update(any())).thenAnswer(it -> it.getArgument(0));

        RefundOperationRecordUpdateReq req = new RefundOperationRecordUpdateReq();
        req.setId(5L);
        req.setOperationContent("新内容");
        req.setReason("补充说明");

        RefundOperationRecordVO vo = refundOperationRecordService.update(req);

        assertEquals("新内容", vo.getOperationContent());
        assertEquals("补充说明", vo.getReason());
        assertEquals(99L, vo.getRefundId());
        assertEquals("SPU9", vo.getSpuOrderNo());
        assertEquals(RefundEnum.State.RECEIVE_WAIT, vo.getAfterState());
    }

    @Test
    @DisplayName("修改: 原记录不存在抛不存在异常")
    void updateShouldRejectMissingRecord() {
        when(refundOperationRecordDomain.findById(6L)).thenReturn(null);
        RefundOperationRecordUpdateReq req = new RefundOperationRecordUpdateReq();
        req.setId(6L);

        PlatformException ex = assertThrows(PlatformException.class, () -> refundOperationRecordService.update(req));

        assertEquals(BaseErrorCode.NODATA.getCode().toString(), ex.getCode());
        verify(refundOperationRecordDomain, never()).update(any());
    }

    @Test
    @DisplayName("按售后单查询: 领域列表转 VO 列表")
    void listByRefundIdShouldTransfer() {
        RefundOperationRecord record = new RefundOperationRecord();
        record.setId(1L);
        record.setRefundId(20L);
        when(refundOperationRecordDomain.listByRefundId(20L)).thenReturn(List.of(record));

        List<RefundOperationRecordVO> voList = refundOperationRecordService.listByRefundId(20L);

        assertEquals(1, voList.size());
        assertEquals(20L, voList.get(0).getRefundId());
    }

    @Test
    @DisplayName("分页: 领域分页转 VO 分页")
    void pageQueryShouldTransferPage() {
        RefundOperationRecordPageReq req = new RefundOperationRecordPageReq();
        Page<RefundOperationRecord> page = new Page<>(1, 10);
        RefundOperationRecord record = new RefundOperationRecord();
        record.setId(3L);
        page.setRecords(List.of(record));
        page.setTotal(1);
        when(refundOperationRecordDomain.pageQuery(req)).thenReturn(page);

        Page<RefundOperationRecordVO> voPage = refundOperationRecordService.pageQuery(req);

        assertNotNull(voPage);
        assertEquals(1, voPage.getTotal());
        assertEquals(3L, voPage.getRecords().get(0).getId());
    }

    @Test
    @DisplayName("删除: 透传领域层")
    void deleteShouldDelegate() {
        refundOperationRecordService.delete(4L);

        verify(refundOperationRecordDomain).delete(4L);
    }
}
