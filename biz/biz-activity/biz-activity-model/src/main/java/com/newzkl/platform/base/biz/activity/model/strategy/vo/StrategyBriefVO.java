package com.newzkl.platform.base.biz.activity.model.strategy.vo;

import lombok.Data;

/**
 * @Description: 策略简要信息
 * @Author: niu
 * @Date: 2024/1/10 16:06
 */
@Data
public class StrategyBriefVO {

    /**
     * 策略描述
     */
    private String strategyDesc;

    /**
     * 策略方式 0：奖金池贡献值  1：甄选师等级分配
     */
    private Integer strategyMode;

    /**
     * 发放奖品方式「1:即时、2:定时、3:人工
     */
    private Integer grantType;

    /**
     * 发放奖品时间
     */
    private Long grantDate;

    /**
     * 扩展信息
     */
    private String extInfo;
}
