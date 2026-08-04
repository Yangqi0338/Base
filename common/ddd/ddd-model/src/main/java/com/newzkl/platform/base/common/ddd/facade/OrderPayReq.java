package com.newzkl.platform.base.common.ddd.facade;


import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author niu
 * @description: 订单支付请求
 * @date 2023/12/19 15:08
 */
@Data
public class OrderPayReq implements Serializable {

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 消费类型
     */
    private EarningsEnum.ConsumeType consumeType;

    /**
     * 订单金额
     */
    private Money orderAmount;

    /**
     * 支付金额
     */
    private Money payAmount;

    /**
     * 订单信息(用于系统内部查看)
     */
    private String orderInfo;

    /**
     * 用户拉起支付看到的
     */
    private String goodsInfo;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 手机号
     */
    private Long accountMobile;

    /**
     * 注册时间。用户在商户系统中的注册时间， 格式须为yyyyMMddHHmmss， 24小时制。
     */
    private String registerTime;


    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 支付方式  1:微信支付  2：支付宝支付
     */
    private OrderEnum.PayType payType;

    /**
     * 支付单号
     */
    private Long tradeNo;

    /**
     * 渠道商id
     */
    private Long channelId;
}
