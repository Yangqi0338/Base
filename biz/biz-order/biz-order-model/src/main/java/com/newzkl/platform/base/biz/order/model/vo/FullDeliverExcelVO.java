package com.newzkl.platform.base.biz.order.model.vo;

import lombok.Data;

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
