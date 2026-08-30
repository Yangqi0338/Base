package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 余额支付请求 (facade 自带 model, 防腐: 金额 Integer 分, 枚举降级为类型码)。
 *
 * @author KC
 */
@Data
public class BalancePayReq implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户类型码
     */
    private PurseEnum.User accountType;

    /**
     * 扣款科目, 必传
     * <p>
     * 2026-08-28 由 {@code Integer} 改真枚举: 下游 {@code AccountPurseAlterRecordReq.purseType} 是
     * {@code PurseEnum.Type}, 原 {@code Integer} 靠 {@code TransferUtils}(hutool 浅拷贝)跨类型转换碰运气。
     * 同类型后拷贝语义确定, 传错编译期即报错
     */
    private PurseEnum.Type purseType;

    /**
     * 支付金额 (分)
     */
    private Money payAmount;

    /**
     * 商品金额 (分)
     */
    private Money goodsAmount;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 订单信息
     */
    private String orderInfo;

    /**
     * 运营商id (仅客户类型为渠道商时传入)
     */
    private Long operatorId;

    /**
     * 用户id
     */
    private Long memberId;
}
