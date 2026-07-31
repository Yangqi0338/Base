package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 本期奖金池
 *
 * @author niu
 */
@Data
@TableName("bonus_pool_now")
public class BonusPoolNowDO implements Serializable {
    /**
     * 本期奖金池id
     */
    private Long id;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 奖金池活动描述
     */
    private String bonusDesc;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 其他配置
     */
    private Long otherConfig;

    /**
     * 订单奖金
     */
    private Integer orderBonus;

    /**
     * 自定义奖金
     */
    private Integer customBonus;

    /**
     * 最终结算奖金
     */
    private Integer settleBonus;

    /**
     * 状态 0：进行中  1：已结算  2：已作废
     */
    private Integer state;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}
