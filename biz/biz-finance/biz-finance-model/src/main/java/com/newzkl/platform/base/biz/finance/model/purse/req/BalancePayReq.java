package com.newzkl.platform.base.biz.finance.model.purse.req;


import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 余额支付请求
 *
 * @author niu
 * @date 2024/1/10 10:15
 */
@Data
public class BalancePayReq implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户类型
     */
    private PurseEnum.User accountType;

    /**
     * 账户类型, 必传
     * <p>
     * 2026-08-28 撤销原默认值 {@code PurseEnum.Type.PURCHASE}: 金额科目决定钱从哪个账户扣,
     * 默认值会让「新增支付场景忘了设科目」静默从采购金扣走, 必须由调用方显式声明
     */
    private PurseEnum.Type purseType;

    /**
     * 支付金额
     */
    private Money payAmount;

    /**
     * 商品金额
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
     * 运营商id
     * @ext 仅客户类型为渠道商时传入; 涉及所属运营商财务模式, 暂定指定传值
     */
    private Long operatorId;

    /**
     * 用户id
     */
    private Long memberId;
}
