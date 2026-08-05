package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 策略
 *
 * @author niu
 */
@Data
@TableName("strategy")
public class StrategyDO implements Serializable {
    /**
     * 策略id
     */
    @TableId(value = "strategy_id", type = IdType.INPUT)
    private Long strategyId;

    /**
     * 策略描述
     */
    private String strategyDesc;

    /**
     * 策略方式 0：奖金池贡献值  1：暂未其他
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
    private Integer orderAmountsRate;

    /**
     * 分红周期 {@link ActivityEnum.DividendCycle}
     */
    private String dividendCycle;

    /**
     * 分红策略 {@link ActivityEnum.SettlementStrategy}
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
     * 分红用户
     */
    private String dividendUser;


    /**
     * 创建人
     */
    private Long creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
