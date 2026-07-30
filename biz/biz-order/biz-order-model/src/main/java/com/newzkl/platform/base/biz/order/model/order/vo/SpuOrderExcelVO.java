package com.newzkl.platform.base.biz.order.model.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * SPU 订单导出 Excel 对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.excel.SpuOrderExcelVO}, 表头文案、列宽、
 * 列顺序逐字保持不变。金额列在装配时已由「分」换算为「元」字符串 (2 位小数, 向下截断),
 * 与旧 {@code OrderController#data} 的换算一致。</p>
 *
 * @author KC
 */
@Getter
@Setter
@EqualsAndHashCode
public class SpuOrderExcelVO {

    /**
     * SPU 订单主键 ID
     */
    @ExcelProperty("订单ID")
    @ColumnWidth(value = 20)
    private String id;

    /**
     * 外部订单号
     */
    @ExcelProperty("外部订单号")
    @ColumnWidth(value = 20)
    private String outOrderNo;

    /**
     * 货款金额 (元)
     */
    @ExcelProperty("货款金额")
    @ColumnWidth(value = 10)
    private String supplierAmount;

    /**
     * 运费金额 (元)
     */
    @ExcelProperty("运费金额")
    @ColumnWidth(value = 10)
    private String freightAmount;

    /**
     * SKU 购买数量合计
     */
    @ExcelProperty("数量")
    @ColumnWidth(value = 8)
    private String count;

    /**
     * 订单状态中文描述
     */
    @ExcelProperty("订单状态")
    @ColumnWidth(value = 12)
    private String orderState;

    /**
     * 收货人姓名
     */
    @ExcelProperty("收货人姓名")
    @ColumnWidth(value = 15)
    private String shipName;

    /**
     * 收货人手机号
     */
    @ExcelProperty("收货人手机号")
    @ColumnWidth(value = 20)
    private String shipPhone;

    /**
     * 收货地址 (地区 + 详细地址)
     */
    @ExcelProperty("收货地址")
    @ColumnWidth(value = 40)
    private String shipArea;

    /**
     * SPU 名称
     */
    @ExcelProperty("SPU名称")
    @ColumnWidth(value = 20)
    private String spuName;

    /**
     * 订单备注
     */
    @ExcelProperty("订单备注")
    @ColumnWidth(value = 20)
    private String remark;
}
