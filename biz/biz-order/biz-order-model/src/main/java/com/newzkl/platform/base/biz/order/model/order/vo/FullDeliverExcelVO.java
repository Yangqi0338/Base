package com.newzkl.platform.base.biz.order.model.order.vo;

import lombok.Data;

/**
 * 整单发货导入 Excel 行对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.excel.FullDeliverExcelVO}。
 * 无 {@code @ExcelProperty} 注解, 沿用 EasyExcel 按字段声明顺序映射列的旧行为, 字段顺序不可调整。</p>
 *
 * @author KC
 */
@Data
public class FullDeliverExcelVO {

    /**
     * SPU订单ID
     */
    private String spuOrderId;

    /**
     * 物流公司名称
     */
    private String expressCompanyName;

    /**
     * 物流单号
     */
    private String expressNo;
}
