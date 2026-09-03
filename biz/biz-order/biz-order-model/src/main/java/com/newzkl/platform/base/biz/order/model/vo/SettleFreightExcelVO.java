package com.newzkl.platform.base.biz.order.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class SettleFreightExcelVO {
    @ExcelProperty("订单号")
    @ColumnWidth(value = 20)
    private String orderNo;
    @ExcelProperty("结算运费总额")
    @ColumnWidth(value = 20)
    private String orderMoney;
}
