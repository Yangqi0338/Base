package com.newzkl.platform.base.biz.order.model.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单明细Excel对象
 */
@Getter
@Setter
@EqualsAndHashCode
public class RefundExcelVO {
    @ExcelProperty("订单ID")
    @ColumnWidth(value = 20)
    private String id;
    @ExcelProperty("订单号")
    @ColumnWidth(value = 20)
    private String orderNo;
    @ExcelProperty("订单类型")
    @ColumnWidth(value = 25)
    private String orderType;
    @ExcelProperty("渠道类型")
    @ColumnWidth(value = 25)
    private String spuChannelType;
    @ExcelProperty("售后状态")
    @ColumnWidth(value = 15)
    private String refundState;
    @ExcelProperty("售后类型")
    @ColumnWidth(value = 15)
    private String refundType;
    @ExcelProperty("售后运费金额")
    @ColumnWidth(value = 15)
    private String freightAmount;
    @ExcelProperty("售后金额")
    @ColumnWidth(value = 15)
    private String refundAmount;
    @ExcelProperty("货款金额")
    @ColumnWidth(value = 15)
    private String supplierAmount;
    @ExcelProperty("服务费")
    @ColumnWidth(value = 15)
    private String serviceAmount;
    @ExcelProperty("售后原因")
    @ColumnWidth(value = 100)
    private String reason;
    @ExcelProperty("申请说明")
    @ColumnWidth(value = 100)
    private String remark;
}
