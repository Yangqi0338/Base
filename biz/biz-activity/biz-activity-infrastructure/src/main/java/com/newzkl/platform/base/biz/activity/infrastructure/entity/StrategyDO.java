package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
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
@TableName
public class StrategyDO extends BaseDO {
    /**
     * 策略描述
     */
    private String strategyDesc;

    /**
     * 策略方式
     * @ext 0 奖金池贡献值, 1 暂未其他
     */
    private Integer strategyMode;

    /**
     * 发放奖品方式
     * @ext 1 即时, 2 定时, 3 人工
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
     * 分红周期
     */
    private ActivityEnum.DividendCycle dividendCycle;

    /**
     * 分红策略
     */
    private ActivityEnum.SettlementStrategy settlementStrategy;

    /**
     * 分红方式
     */
    private ActivityEnum.DividendMethod dividendMethod;

    /**
     * 分红角色
     */
    private RoleEnum.CompanyRole dividendRole;

    /**
     * 分红用户
     */
    private String dividendUser;
}
