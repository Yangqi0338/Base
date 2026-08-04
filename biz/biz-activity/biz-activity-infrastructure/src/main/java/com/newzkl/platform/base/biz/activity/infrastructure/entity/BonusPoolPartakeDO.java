package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 奖金池参与记录
 *
 * @author niu
 */
@Data
@TableName("bonus_pool_partake")
public class BonusPoolPartakeDO implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 账户id
     */
    private Long accountId;

    /**
     * 流水号id
     */
    private String serialId;


    /**
     * 活动id
     */
    private String activityId;

    /**
     * 活动名称
     */
    private String activityName;


    /**
     * 结算id
     */
    private String settlementId;


    /**
     * 结算名称
     */
    private String settlementName;


    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号 phone
     */
    private String accountName;

    /**
     * 角色
     */
    private Long role;

    /**
     * 角色名称
     */
    private String roleName;


    /**
     * 个人分红比例
     */
    private Integer personPercent;

    /**
     * 个人分红金额 (Money, 落库 BIGINT 分)
     */
    private Money dividendAmount;

    /**
     * 分红周期
     */
    private String dividendCycle;


    /**
     * 奖金池id
     */
    private Long bonusPoolId;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 交易单号
     */
    private String orderSn;

    /**
     * 参与sku
     */
    private Long skuCode;

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 录入奖金 (Money, 落库 BIGINT 分)
     */
    private Money bonus;

    /**
     * 录入时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
