package com.newzkl.platform.base.biz.sys.model.config.vo;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 数智门店配置 VO
 *
 * <p>本地化自旧 {@code com.zkl.scm.rpc.user.ChannelConfigVO}, 以 JSON 串存于字典
 * {@code DictEnum.Key.CHANNEL_CONFIG}。字段与 biz-store / biz-finance 的同名 VO 逐字一致,
 * 保证写入端 (本域) 与读取端 (biz-store/biz-finance) 反序列化同构。</p>
 *
 * <p>金额字段已 Money 化 (JSON 元字符串 "11.11"); discount/purchaseMinimumNum/withdrawalFee
 * 非金额 (折扣/数量/千分制手续费) 保持 Integer。已持久化的旧 分-integer 字典值需 分→元 重写,
 * 见迁移清单。</p>
 *
 * @author KC
 */
@Data
public class ChannelConfigVO implements Serializable {

    /**
     * 系统售价
     */
    private Money systemPrice;

    /**
     * 系统原价
     */
    private Money systemOriginalPrice;

    /**
     * 折扣力度 (折, 非金额)
     */
    private Integer discount;

    /**
     * 采购金充值最低金额
     */
    private Money minimumRechargeAmount;

    /**
     * 商品席位原价
     */
    private Money seatOriginalPrice;

    /**
     * 席位购买最小数量 (数量, 非金额)
     */
    private Integer purchaseMinimumNum;

    /**
     * 最小提现金额
     */
    private Money minimumWithdrawalAmount;

    /**
     * 提现手续费, 千分制 (15 = 0.015, 比率非金额)
     */
    private Integer withdrawalFee;

    /**
     * 单日提现最高金额
     */
    private Money maximumDailyWithdrawalAmount;
}
