package com.newzkl.platform.base.biz.order.model.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

/**
 * SPU 维度售后统计(替 SpuRefundRes, SpuOrder 层折叠后按 order + spu 聚合)
 *
 * @author KC
 */
@Data
public class OrderRefundRes {
    /**
     * 商品ID
     */
    private Long spuId;
    /**
     * 交易单号
     */
    private String orderNo;
    /**
     * 购买数量合计
     */
    private Integer orderCount;
    /**
     * 售后中数量合计
     */
    private Integer refundingCount;
    /**
     * 已售后数量合计
     */
    private Integer refundedCount;
    /**
     * 已发货数量合计
     */
    private Integer deliverCount;
    /**
     * 运费金额
     */
    private Money freightAmount;
}
