package com.newzkl.platform.base.biz.order.model.order.res;

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
    private String spuOrderNo;
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
     * 运费金额
     */
    private Integer freightAmount;

    /**
     * 商品快照（JSON格式：SPU名称/图片/规格等）
     */
    private String goodsSnapshot;
}
