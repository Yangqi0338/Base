package com.newzkl.platform.base.biz.order.model.vo;

import lombok.Data;

@Data
public class SplitDeliverExcelVO {
    /**
     * SPU订单ID
     */
    private String spuOrderId;
    /**
     * SKU_ID
     */
    private String skuId;
    /**
     * 快递公司名称
     *
     * <p>必须是快递公司编码表里的名称 (或编码), 识别不出该行导入失败并返回行号</p>
     */
    private String expressCompanyName;
    /**
     * 快递单号
     */
    private String expressNo;
}
