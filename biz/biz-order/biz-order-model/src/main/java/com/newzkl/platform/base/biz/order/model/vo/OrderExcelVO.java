package com.newzkl.platform.base.biz.order.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单导出Excel对象(替 SpuOrderExcelVO)
 * @author KC
 */
@Getter
@Setter
@EqualsAndHashCode
public class OrderExcelVO {

    @ExcelProperty("订单ID")
    @ColumnWidth(value = 20)
    private String id;
    @ExcelProperty("外部订单号")
    @ColumnWidth(value = 20)
    private String outOrderNo;
    @ExcelProperty("货款金额")
    @ColumnWidth(value = 10)
    private String supplierAmount;
    @ExcelProperty("运费金额")
    @ColumnWidth(value = 10)
    private String freightAmount;
    @ExcelProperty("数量")
    @ColumnWidth(value = 8)
    private String count;
    @ExcelProperty("订单状态")
    @ColumnWidth(value = 12)
    private String orderState;
    @ExcelProperty("收货人姓名")
    @ColumnWidth(value = 15)
    private String shipName;
    @ExcelProperty("收货人手机号")
    @ColumnWidth(value = 20)
    private String shipPhone;
    @ExcelProperty("收货地址")
    @ColumnWidth(value = 40)
    private String shipArea;
    @ExcelProperty("SPU名称")
    @ColumnWidth(value = 20)
    private String spuName;
    @ExcelProperty("订单备注")
    @ColumnWidth(value = 20)
    private String remark;
}
