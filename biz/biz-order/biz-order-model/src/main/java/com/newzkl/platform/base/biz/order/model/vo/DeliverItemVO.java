package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 发货单明细
 * @author fang
 */
@Data
public class DeliverItemVO extends BaseRes {
     /**
     * 主键
     */
     private Long id;
     /**
     * SPU订单号
     */
     private Long spuOrderId;
     /**
     * 发货单ID
     */
     private Long deliverId;
     /**
     * skuId
     */
     private Long skuId;
     /**
     * 发货数量
     */
     private Integer count;
}