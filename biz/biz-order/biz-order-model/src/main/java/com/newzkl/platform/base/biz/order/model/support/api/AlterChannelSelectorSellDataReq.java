package com.newzkl.platform.base.biz.order.model.support.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @author niu
 * @description: 更新渠道商选品销售数据
 * @date 2024/4/25 14:36
 */
@Data
public class AlterChannelSelectorSellDataReq implements Serializable {

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 市场id
     */
    private Long marketId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 数量
     */
    private Integer sellNum = 0;

    /**
     * 金额（包含服务费）
     */
    private Integer sellAmount = 0;
}
