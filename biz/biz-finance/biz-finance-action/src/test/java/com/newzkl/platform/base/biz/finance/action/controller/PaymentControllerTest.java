package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.model.pay.res.PurchasePaymentExportVO;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PaymentVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link PaymentController} 导出行组装测试。
 *
 * @author KC
 */
class PaymentControllerTest {

    @Test
    @DisplayName("导出行: 金额分转元保留两位, 支付状态翻译中文")
    void buildExportRowsShouldConvertAmountAndPayState() {
        PaymentVO vo = new PaymentVO();
        vo.setAccountId(1001L);
        vo.setPayAmount(12345);
        vo.setPayState(1);
        LocalDateTime payTime = LocalDateTime.of(2026, 7, 26, 10, 30);
        vo.setPayTime(payTime);

        List<PurchasePaymentExportVO> rows = PaymentController.buildExportRows(List.of(vo));

        assertEquals(1, rows.size());
        PurchasePaymentExportVO row = rows.get(0);
        assertEquals(1001L, row.getAccountId());
        assertEquals("123.45", row.getPayAmount());
        assertEquals("支付成功", row.getPayState());
        assertEquals(payTime, row.getPayTime());
    }

    @Test
    @DisplayName("导出行: 金额为整元时补足两位小数")
    void buildExportRowsShouldKeepTwoDecimalScale() {
        PaymentVO vo = new PaymentVO();
        vo.setPayAmount(10000);
        vo.setPayState(0);

        PurchasePaymentExportVO row = PaymentController.buildExportRows(List.of(vo)).get(0);

        assertEquals("100.00", row.getPayAmount());
        assertEquals("待支付", row.getPayState());
    }

    @Test
    @DisplayName("导出行: 金额/状态为空不抛异常, 输出空值")
    void buildExportRowsShouldTolerateNullFields() {
        PaymentVO vo = new PaymentVO();

        PurchasePaymentExportVO row = PaymentController.buildExportRows(List.of(vo)).get(0);

        assertNull(row.getPayAmount());
        assertNull(row.getPayState());
    }

    @Test
    @DisplayName("导出行: 未识别的支付状态码输出空值而非抛异常")
    void buildExportRowsShouldTolerateUnknownPayState() {
        PaymentVO vo = new PaymentVO();
        vo.setPayState(99);

        PurchasePaymentExportVO row = PaymentController.buildExportRows(List.of(vo)).get(0);

        assertNull(row.getPayState());
    }

    @Test
    @DisplayName("导出行: 空列表返回空集合而非 null")
    void buildExportRowsShouldReturnEmptyListForEmptyInput() {
        assertTrue(PaymentController.buildExportRows(List.of()).isEmpty());
    }
}
