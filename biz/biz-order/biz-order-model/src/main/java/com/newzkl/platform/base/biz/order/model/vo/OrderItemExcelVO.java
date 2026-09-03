package com.newzkl.platform.base.biz.order.model.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单明细Excel对象(替 SpuOrderItemExcelVO)
 * @author KC
 */
@Getter
@Setter
@EqualsAndHashCode
public class OrderItemExcelVO {
    @ExcelProperty("订单ID")
    @ColumnWidth(value = 20)
    private String id;
    @ExcelProperty("外部订单号")
    @ColumnWidth(value = 20)
    private String outOrderNo;
    @ExcelProperty("SKU订单状态")
    @ColumnWidth(value = 25)
    private String orderState;
    @ExcelProperty("售后中(黄条表示存在售后)")
    @ColumnWidth(value = 15)
    private String refunding;
    @ExcelProperty("商品名称")
    @ColumnWidth(value = 20)
    private String spuName;
    @ExcelProperty("规格值")
    @ColumnWidth(value = 15)
    private String attribute;
    @ExcelProperty("SKU_ID")
    @ColumnWidth(value = 20)
    private String skuId;
    @ExcelProperty("单价")
    @ColumnWidth(value = 10)
    private String price;
    @ExcelProperty("数量")
    @ColumnWidth(value = 8)
    private String count;
    @ExcelProperty("收货人姓名")
    @ColumnWidth(value = 15)
    private String shipName;
    @ExcelProperty("收货人手机号")
    @ColumnWidth(value = 20)
    private String shipPhone;
    @ExcelProperty("收货地址")
    @ColumnWidth(value = 40)
    private String shipArea;
    @ExcelProperty("订单备注")
    @ColumnWidth(value = 20)
    private String remark;
    @ExcelIgnore
    private ShipVO shipVO;
    @ExcelIgnore
    private Integer refundingCount;
}
