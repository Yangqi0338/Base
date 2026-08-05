package com.newzkl.platform.base.biz.activity.model.event.vo;

import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 策略信息配置
 * @Author: niu
 * @Date: 2024/1/9 17:02
 */
@Data
public class StrategyVO {

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 策略描述
     */
    private String strategyDesc;

    /**
     * 策略方式 0：会员等级等总体占比  1：暂未其他
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

    /**
     * 订单金额比例
     */
    private BigDecimal orderAmountsRate;

    /**
     * 分红周期 {@link ActivityEnum.DividendCycle}
     */
    private String dividendCycle;

    /**
     * 分红策略 {@link ActivityEnum.SettlementStrategy}
     *
     */
    private String settlementStrategy;

    /**
     * 分红方式 {@link ActivityEnum.DividendMethod}
     */
    private String dividendMethod;

    /**
     * 分红角色
     */
    private String dividendRole;

    /**
     * 策略详情
     */
    private List<StrategyDetailVO> strategyDetails;

}
