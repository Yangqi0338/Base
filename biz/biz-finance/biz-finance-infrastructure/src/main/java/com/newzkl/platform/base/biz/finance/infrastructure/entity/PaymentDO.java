package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.handler.RawJsonStringTypeHandler;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;

/**
 * 支付单
 * @author kc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class PaymentDO extends BaseDO {
    /**
     * 交易单号
     */
    @Index
    private Long tradeNo;

    /**
     * 订单号
     */
    @Index
    private Long orderNo;

    /**
     * 客户id
     */
    @Index
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
     * 消费类型
     */
    @Index
    private EarningsEnum.ConsumeType consumeType;

    /**
     * 支付状态
     */
    private PaymentEnum.PayState payState;

    /**
     * 三方交易单号
     */
    private String tripartiteTradeNo;

    /**
     * 订单信息
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String orderInfo;

    /**
     * 收款方信息
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String payeeInfo;

    /**
     * 支付时间
     */
    @Index
    private LocalDateTime payTime;

}