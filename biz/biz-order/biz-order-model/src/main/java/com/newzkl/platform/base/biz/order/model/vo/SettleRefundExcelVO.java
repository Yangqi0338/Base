package com.newzkl.platform.base.biz.order.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class SettleRefundExcelVO {
    @ExcelProperty("售后单")
    @ColumnWidth(value = 20)
    private String refundId;
    @ExcelProperty("关联订单号")
    @ColumnWidth(value = 15)
    private String orderNo;
    @ExcelProperty("售后总额")
    @ColumnWidth(value = 15)
    private String orderMoney;
    @ExcelProperty("结算总额")
    @ColumnWidth(value = 15)
    private String c4;
}
