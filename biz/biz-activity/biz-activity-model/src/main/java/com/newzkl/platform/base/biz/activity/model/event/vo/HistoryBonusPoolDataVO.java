package com.newzkl.platform.base.biz.activity.model.event.vo;

import lombok.Data;

/**
 * @Description: 历史奖金池数据
 * @Author: niu
 * @Date: 2024/1/16 16:01
 */
@Data
public class HistoryBonusPoolDataVO {

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
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 奖金池描述
     */
    private String desc;

    /**
     * 奖金池状态 0：进行中  1：已结算  2：已作废
     */
    private Integer state;

    /**
     * 结算方式 0：未结算 1：订单  2：自定义
     */
    private Integer settleType;

}
