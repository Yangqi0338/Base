package com.newzkl.platform.base.biz.finance.model.pay.res;

import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 交易单信息
 * @date 2023/12/22 16:11
 */
@Data
public class TradeOrderInfoRes implements Serializable {

    /**
     * 交易单号
     */
    private Long tradeNo;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 支付金额
     */
    private Money payAmount;

    /**
     * 消费类型
     */
    private EarningsEnum.ConsumeType consumeType;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 订单信息
     */
    private String orderInfo;
}
