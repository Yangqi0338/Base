package com.newzkl.platform.base.biz.activity.model.event.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Description: 奖金池数据
 * @Author: niu
 * @Date: 2024/1/9 15:10
 */
@Data
public class BonusPoolDataVO {

    /**
     * 奖金池id
     */
    private Long bonusPoolId;

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
     * 开始时间
     */
    private LocalDate startTime;

    /**
     * 结束时间
     */
    private LocalDate endTime;

    /**
     * 奖金池描述
     */
    private String desc;

    /**
     * 奖金池状态 0：进行中  1：已结算  2：已作废
     */
    private String state;

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 其他配置
     */
    private Long otherConfig;
}
