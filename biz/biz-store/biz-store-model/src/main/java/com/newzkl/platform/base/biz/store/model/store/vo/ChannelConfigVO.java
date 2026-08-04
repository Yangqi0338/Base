package com.newzkl.platform.base.biz.store.model.store.vo;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 渠道商配置 VO
 *
 * <p>本地化自旧 {@code com.zkl.scm.rpc.user.ChannelConfigVO}, 从字典 JSON 反序列化。
 * 字段与 biz-sys (写端) / biz-finance 的同名 VO 逐字一致, 保证反序列化同构。</p>
 *
 * <p>金额字段已 Money 化 (JSON 元字符串); discount/purchaseMinimumNum/withdrawalFee
 * 非金额保持 Integer。旧 分-integer 字典值需 分→元 重写, 见迁移清单。</p>
 *
 * @author KC
 */
@Data
public class ChannelConfigVO implements Serializable {

    /**
     * 系统价
     */
    private Money systemPrice;

    /**
     * 系统原价
     */
    private Money systemOriginalPrice;

    /**
     * 折扣 (非金额)
     */
    private Integer discount;

    /**
     * 最低充值金额
     */
    private Money minimumRechargeAmount;

    /**
     * 席位原价
     */
    private Money seatOriginalPrice;

    /**
     * 最低购买数量 (数量, 非金额)
     */
    private Integer purchaseMinimumNum;

    /**
     * 最低提现金额
     */
    private Money minimumWithdrawalAmount;

    /**
     * 提现手续费, 千分制 15 = 0.015 (比率非金额)
     */
    private Integer withdrawalFee;

    /**
     * 每日最大提现金额
     */
    private Money maximumDailyWithdrawalAmount;
}
