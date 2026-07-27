package com.newzkl.platform.base.biz.order.model.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 结算类型明细 - 运费导出 Excel 对象。
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.excel.SettleFreightExcelVO}。列名与列宽保持不变。</p>
 *
 * @author KC
 */
@Getter
@Setter
@EqualsAndHashCode
public class SettleFreightExcelVO {

    /**
     * 订单号 (旧列名"订单ID")
     */
    @ExcelProperty("订单ID")
    @ColumnWidth(value = 20)
    private String spuOrderId;

    /**
     * 结算运费总额
     */
    @ExcelProperty("结算运费总额")
    @ColumnWidth(value = 20)
    private String orderMoney;
}
