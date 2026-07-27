package com.newzkl.platform.base.biz.order.model.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 结算类型明细 - 售后冲正导出 Excel 对象。
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.excel.SettleRefundExcelVO}。列名与列宽保持不变,
 * {@code c4} 为"结算总额"(负值冲正)。</p>
 *
 * @author KC
 */
@Getter
@Setter
@EqualsAndHashCode
public class SettleRefundExcelVO {

    /**
     * 售后单 ID
     */
    @ExcelProperty("售后单")
    @ColumnWidth(value = 20)
    private String refundId;

    /**
     * 关联订单号 (旧列名"关联订单")
     */
    @ExcelProperty("关联订单")
    @ColumnWidth(value = 15)
    private String spuOrderId;

    /**
     * 售后总额
     */
    @ExcelProperty("售后总额")
    @ColumnWidth(value = 15)
    private String orderMoney;

    /**
     * 结算总额 (冲正负值)
     */
    @ExcelProperty("结算总额")
    @ColumnWidth(value = 15)
    private String c4;
}
