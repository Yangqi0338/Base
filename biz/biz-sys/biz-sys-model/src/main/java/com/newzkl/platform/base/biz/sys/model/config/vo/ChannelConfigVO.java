package com.newzkl.platform.base.biz.sys.model.config.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 数智门店配置 VO
 *
 * <p>本地化自旧 {@code com.zkl.scm.rpc.user.ChannelConfigVO}, 以 JSON 串存于字典
 * {@code DictEnum.Key.CHANNEL_CONFIG}。字段与 biz-store 的同名 VO 逐字一致,
 * 保证写入端 (本域) 与读取端 (biz-store) 反序列化同构。</p>
 *
 * @author KC
 */
@Data
public class ChannelConfigVO implements Serializable {

    /**
     * 系统售价
     */
    private Integer systemPrice;

    /**
     * 系统原价
     */
    private Integer systemOriginalPrice;

    /**
     * 折扣力度 (折)
     */
    private Integer discount;

    /**
     * 采购金充值最低金额
     */
    private Integer minimumRechargeAmount;

    /**
     * 商品席位原价
     */
    private Integer seatOriginalPrice;

    /**
     * 席位购买最小数量
     */
    private Integer purchaseMinimumNum;

    /**
     * 最小提现金额
     */
    private Integer minimumWithdrawalAmount;

    /**
     * 提现手续费, 千分制 (15 = 0.015)
     */
    private Integer withdrawalFee;

    /**
     * 单日提现最高金额
     */
    private Integer maximumDailyWithdrawalAmount;
}
