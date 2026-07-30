package com.newzkl.platform.base.biz.store.model.store.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 渠道商配置 VO
 *
 * <p>本地化自旧 {@code com.zkl.scm.rpc.user.ChannelConfigVO}, 从字典 JSON 反序列化。</p>
 *
 * @author KC
 */
@Data
public class ChannelConfigVO implements Serializable {

    /**
     * 系统价
     */
    private Integer systemPrice;

    /**
     * 系统原价
     */
    private Integer systemOriginalPrice;

    /**
     * 折扣
     */
    private Integer discount;

    /**
     * 最低充值金额
     */
    private Integer minimumRechargeAmount;

    /**
     * 席位原价
     */
    private Integer seatOriginalPrice;

    /**
     * 最低购买数量
     */
    private Integer purchaseMinimumNum;

    /**
     * 最低提现金额
     */
    private Integer minimumWithdrawalAmount;

    /**
     * 提现手续费, 15 = 0.015
     */
    private Integer withdrawalFee;

    /**
     * 每日最大提现金额
     */
    private Integer maximumDailyWithdrawalAmount;
}
