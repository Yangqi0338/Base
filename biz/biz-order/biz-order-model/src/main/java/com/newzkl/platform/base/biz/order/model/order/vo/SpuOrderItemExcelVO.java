package com.newzkl.platform.base.biz.order.model.order.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * SPU 订单明细 (SKU 粒度) 导出 Excel 对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.domain.order.model.excel.SpuOrderItemExcelVO},
 * 表头文案、列宽、列顺序逐字保持不变。旧类误引 {@code jdk.nashorn...Ignore} 的无用 import
 * 已去除。{@code refundingCount} 仍保留为忽略列, 供导出层判定黄条高亮行。</p>
 *
 * @author KC
 */
@Getter
@Setter
@EqualsAndHashCode
public class SpuOrderItemExcelVO {

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
     * SKU 订单状态中文描述
     */
    @ExcelProperty("SKU订单状态")
    @ColumnWidth(value = 25)
    private String orderState;

    /**
     * 是否存在售后 (是 / 否)
     */
    @ExcelProperty("售后中(黄条表示存在售后)")
    @ColumnWidth(value = 15)
    private String refunding;

    /**
     * 商品名称
     */
    @ExcelProperty("商品名称")
    @ColumnWidth(value = 20)
    private String spuName;

    /**
     * 规格值 (多个以分号拼接)
     */
    @ExcelProperty("规格值")
    @ColumnWidth(value = 15)
    private String attribute;

    /**
     * SKU ID
     */
    @ExcelProperty("SKU_ID")
    @ColumnWidth(value = 20)
    private String skuId;

    /**
     * 单价 (元)
     */
    @ExcelProperty("单价")
    @ColumnWidth(value = 10)
    private String price;

    /**
     * 购买数量
     */
    @ExcelProperty("数量")
    @ColumnWidth(value = 8)
    private String count;

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
     * 订单备注
     */
    @ExcelProperty("订单备注")
    @ColumnWidth(value = 20)
    private String remark;

    /**
     * 售后中数量 (不导出, 仅用于黄条高亮判定)
     */
    @ExcelIgnore
    private Integer refundingCount;
}
