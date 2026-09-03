package com.newzkl.platform.base.biz.order.facade.model.order;

import lombok.Data;

/**
 * 交易单关系(替 SpuOrderRelationVO, SpuOrder 层折叠后, 语义 order 级)
 * @author muc_fang
 */
@Data
public class OrderRelationVO {
    /**
     * 交易单ID
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
