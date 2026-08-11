package com.newzkl.platform.base.biz.socialbang.model.strategy.req;

import lombok.Data;

/**
 * @Description: 抽取请求对象
 * @Author: niu
 * @Date: 2024/1/8 17:16
 */
@Data
public class DrawReq {

    /**
     * 用户ID
     */
    private Long uId;

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 渠道商id
     */
    private Long channelId;

    public DrawReq(Long strategyId,Long channelId) {
        this.strategyId = strategyId;
        this.channelId = channelId;
    }
}
