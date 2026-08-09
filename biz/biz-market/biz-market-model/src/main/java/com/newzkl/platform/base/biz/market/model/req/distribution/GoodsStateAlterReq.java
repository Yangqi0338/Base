package com.newzkl.platform.base.biz.market.model.req.distribution;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 商品状态更新
 * @date 2024/4/2 11:22
 */
@Data
public class GoodsStateAlterReq {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 更新商品状态
     *
     * @ext 0：下架 1：上架
     */
    private Integer state;

    /**
     * 门店id 不传默认更新所有门店
     */
    private Long storeId;

    /**
     * 渠道商id 前端不需传
     */
    private Long channelId;

    /**
     * 上架时间 前端不需传
     */
    private LocalDateTime upTime;

}
