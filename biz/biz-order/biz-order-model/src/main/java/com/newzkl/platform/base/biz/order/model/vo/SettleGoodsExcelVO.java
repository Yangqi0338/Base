package com.newzkl.platform.base.biz.order.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class SettleGoodsExcelVO {
    @ExcelProperty("订单号")
    @ColumnWidth(value = 20)
    private String orderNo;
    @ExcelProperty("商品信息")
    @ColumnWidth(value = 30)
    private String spuName;
    @ExcelProperty("商品总额")
    @ColumnWidth(value = 15)
    private String orderMoney;
    @ExcelProperty("结算状态")
    @ColumnWidth(value = 15)
    private String c4;
    @ExcelProperty("结算总额")
    @ColumnWidth(value = 15)
    private String c5;
}
