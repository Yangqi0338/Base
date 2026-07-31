package com.newzkl.platform.base.biz.order.facade.model.order;

import lombok.Data;

/**
 * @author muc_fang
 * @Description: SPU订单关系
 * @date 2024/1/1916:50
 */
@Data
public class SpuOrderRelationVO {
    /**
     * SPU订单ID
     */
    private Long id;
    /**
     * 渠道商ID channel_id
     */
    private Long channelId;
    /**
     * 供应商ID supplier_id
     */
    private Long supplierId;
    /**
     * 交易师ID dealer_id
     */
    private Long dealerId;
    /**
     * 运营商ID operator_id
     */
    private Long operatorId;
}
