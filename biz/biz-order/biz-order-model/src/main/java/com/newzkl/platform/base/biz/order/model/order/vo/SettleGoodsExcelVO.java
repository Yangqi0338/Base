package com.newzkl.platform.base.biz.order.model.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 结算类型明细 - 商品导出 Excel 对象。
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.excel.SettleGoodsExcelVO}
 * (旧在 application 模块, 新随其他 Excel VO 统一落 model)。列名与列宽保持不变。</p>
 *
 * @author KC
 */
@Getter
@Setter
@EqualsAndHashCode
public class SettleGoodsExcelVO {

    /**
     * 订单号 (旧列名"订单ID")
     */
    @ExcelProperty("订单ID")
    @ColumnWidth(value = 20)
    private String spuOrderId;

    /**
     * 商品信息 (名称 + 规格 * 数量)
     */
    @ExcelProperty("商品信息")
    @ColumnWidth(value = 30)
    private String spuName;

    /**
     * 商品总额
     */
    @ExcelProperty("商品总额")
    @ColumnWidth(value = 15)
    private String orderMoney;

    /**
     * 结算状态
     */
    @ExcelProperty("结算状态")
    @ColumnWidth(value = 15)
    private String c4;

    /**
     * 结算总额
     */
    @ExcelProperty("结算总额")
    @ColumnWidth(value = 15)
    private String c5;
}
