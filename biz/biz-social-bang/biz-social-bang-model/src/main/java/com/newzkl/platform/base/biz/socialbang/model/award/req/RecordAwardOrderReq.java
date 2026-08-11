package com.newzkl.platform.base.biz.socialbang.model.award.req;

import lombok.Data;

import java.util.Date;

/**
 * @Description: 记录奖品单
 * @Author: niu
 * @Date: 2024/1/17 14:56
 */
@Data
public class RecordAwardOrderReq {

    /**
     * 记录id
     */
    private Long recordId;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 渠道商活动id
     */
    private Long channelActivityId;

    /**
     * 发奖状态
     */
    private Integer awardState;

    /**
     * 发放类型
     */
    private Integer grantType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 奖品id
     * @ext 0 为现金
     */
    private Long awardId = 0L;

    /**
     * 奖品类型
     * @ext 0-现金
     */
    private Integer awardType = 0;

    /**
     * 扩展数据
     */
    private String ext;

    /**
     * 时间
     */
    private Date time;
}
