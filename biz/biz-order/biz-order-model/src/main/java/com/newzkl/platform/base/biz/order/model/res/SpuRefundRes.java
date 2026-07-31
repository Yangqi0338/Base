package com.newzkl.platform.base.biz.order.model.res;

import lombok.Data;

/**
 * @author muc_fang
 * @Description: 订单商品发货信息
 * @date 2023/5/914:58
 */
@Data
public class SpuRefundRes {
    /**
     * SpuID
     */
    private Long spuId;
    /**
     * spu订单id
     */
    private Long spuOrderId;
    /**
     * 购买数量
     */
    private Integer orderCount;
    /**
     * 售后中数量
     */
    private Integer refundingCount;
    /**
     * 已售后数量
     */
    private Integer refundedCount;
    /**
     * 已发货数量
     */
    private Integer deliverCount;
    /**
     * spu_name
     */
    private String spuName;
    /**
     * spu_img
     */
    private String spuImg;
    /**
     * 运费金额
     */
    private Integer freightAmount;
}
