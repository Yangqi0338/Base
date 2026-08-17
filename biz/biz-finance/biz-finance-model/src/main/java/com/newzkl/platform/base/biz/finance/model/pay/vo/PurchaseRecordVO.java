package com.newzkl.platform.base.biz.finance.model.pay.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.finance.model.pay.res.SeatPackageOrderInfo;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;


/**
 * 购买记录 #pay(PurchaseRecord)DTO类
 *
 * @author kc
 * @since 2025-11-25 17:24:22
 */
@Data
public class PurchaseRecordVO extends BaseRes {

    /**
     * 购买单号
     */
    private String purchaseNo;

    /**
     * 购买类型
     */
    private PurseEnum.PurchaseRecordType type;

    /**
     * 交易单号
     */
    private Long tradeNo;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 支付金额
     */
    private Money payAmount;

    /**
     * 商品金额
     */
    private Money goodsAmount;

    /**
     * 支付方式
     */
    private PaymentEnum.PayType payType;

    /**
     * 支付状态
     */
    private OrderEnum.State payState;

    /**
     * 三方交易单号
     */
    private String tripartiteTradeNo;

    /**
     * 订单信息（原始 JSON，不序列化到出参）
     */
    @JsonIgnore
    private String orderInfo;

    /**
     * 席位套餐订单信息（从 orderInfo JSON 反序列化，前端读此字段获取 purchaseNum 等）
     */
    private SeatPackageOrderInfo seatPackageOrderInfo;

}

