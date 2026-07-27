package com.newzkl.platform.base.biz.order.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundOperationRecordRepository;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.biz.order.model.order.dto.RefundOperationRecord;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link RefundOperationRecordDomainImpl} 领域规则测试。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("售后操作记录领域服务")
class RefundOperationRecordDomainImplTest {

    @Mock
    private RefundOperationRecordRepository refundOperationRecordRepository;

    @InjectMocks
    private RefundOperationRecordDomainImpl refundOperationRecordDomain;

    /**
     * 构造一条通过全部必填校验的记录。
     *
     * @return 合法记录
     */
    private RefundOperationRecord validRecord() {
        RefundOperationRecord record = new RefundOperationRecord();
        record.setSpuOrderNo("SPU202607260001");
        record.setRefundId(9001L);
        record.setOperatorRoleCode(1002L);
        record.setOperatorClient("channel");
        record.setAfterState(RefundEnum.State.CHANNEL_WAIT);
        record.setOperationContent("渠道商审核通过");
        return record;
    }

    @Test
    @DisplayName("新增: 校验通过后委托仓储保存")
    void createShouldDelegateToRepository() {
        RefundOperationRecord record = validRecord();
        when(refundOperationRecordRepository.save(any())).thenReturn(record);

        assertNotNull(refundOperationRecordDomain.create(record));
        verify(refundOperationRecordRepository).save(record);
    }

    @Test
    @DisplayName("新增: 售后单 ID 缺失抛参数异常")
    void createShouldRejectMissingRefundId() {
        RefundOperationRecord record = validRecord();
        record.setRefundId(null);

        ScmException ex = assertThrows(ScmException.class, () -> refundOperationRecordDomain.create(record));

        assertEquals(BaseErrorCode.PARAM.getCode().toString(), ex.getCode());
        assertTrue(ex.getMessage().contains("售后单ID"));
        verify(refundOperationRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("新增: 操作内容为空抛参数异常")
    void createShouldRejectBlankContent() {
        RefundOperationRecord record = validRecord();
        record.setOperationContent("  ");

        assertThrows(ScmException.class, () -> refundOperationRecordDomain.create(record));
        verify(refundOperationRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("新增: 携带 ID 抛参数异常 (旧语义: 新增时 ID 必须为空)")
    void createShouldRejectPresetId() {
        RefundOperationRecord record = validRecord();
        record.setId(1L);

        assertThrows(ScmException.class, () -> refundOperationRecordDomain.create(record));
        verify(refundOperationRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("修改: 记录存在则委托仓储更新")
    void updateShouldDelegateWhenExists() {
        RefundOperationRecord record = validRecord();
        record.setId(66L);
        when(refundOperationRecordRepository.existsById(66L)).thenReturn(true);
        when(refundOperationRecordRepository.updateById(any())).thenReturn(record);

        assertNotNull(refundOperationRecordDomain.update(record));
        verify(refundOperationRecordRepository).updateById(record);
    }

    @Test
    @DisplayName("修改: 记录不存在抛不存在异常")
    void updateShouldRejectMissingRecord() {
        RefundOperationRecord record = validRecord();
        record.setId(77L);
        when(refundOperationRecordRepository.existsById(77L)).thenReturn(false);

        ScmException ex = assertThrows(ScmException.class, () -> refundOperationRecordDomain.update(record));

        assertEquals(BaseErrorCode.NODATA.getCode().toString(), ex.getCode());
        verify(refundOperationRecordRepository, never()).updateById(any());
    }

    @Test
    @DisplayName("删除: 记录存在才真正删除")
    void deleteShouldCheckExistsFirst() {
        when(refundOperationRecordRepository.existsById(88L)).thenReturn(true);

        refundOperationRecordDomain.delete(88L);

        verify(refundOperationRecordRepository).deleteById(88L);
    }

    @Test
    @DisplayName("删除: 记录不存在抛不存在异常")
    void deleteShouldRejectMissingRecord() {
        when(refundOperationRecordRepository.existsById(89L)).thenReturn(false);

        assertThrows(ScmException.class, () -> refundOperationRecordDomain.delete(89L));
        verify(refundOperationRecordRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("按售后单查询: 透传仓储结果")
    void listByRefundIdShouldDelegate() {
        when(refundOperationRecordRepository.listByRefundId(9001L)).thenReturn(List.of(validRecord()));

        assertEquals(1, refundOperationRecordDomain.listByRefundId(9001L).size());
    }

    @Test
    @DisplayName("按 SPU 订单号查询: 空单号抛参数异常")
    void listBySpuOrderNoShouldRejectBlank() {
        assertThrows(ScmException.class, () -> refundOperationRecordDomain.listBySpuOrderNo(" "));
    }

    @Test
    @DisplayName("分页: 透传仓储分页结果")
    void pageQueryShouldDelegate() {
        RefundOperationRecordPageReq req = new RefundOperationRecordPageReq();
        when(refundOperationRecordRepository.pageByQuery(req)).thenReturn(new Page<>());

        assertNotNull(refundOperationRecordDomain.pageQuery(req));
        verify(refundOperationRecordRepository).pageByQuery(req);
    }

    @Test
    @DisplayName("分页: 入参为空抛参数异常")
    void pageQueryShouldRejectNull() {
        assertThrows(ScmException.class, () -> refundOperationRecordDomain.pageQuery(null));
    }
}
