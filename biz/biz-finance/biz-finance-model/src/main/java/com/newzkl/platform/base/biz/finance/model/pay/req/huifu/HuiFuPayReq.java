package com.newzkl.platform.base.biz.finance.model.pay.req.huifu;

import com.newzkl.platform.base.biz.finance.model.enums.finance.PayEnum;
import lombok.Data;

/**
 * 汇付支付请求
 *
 * @author niu
 * @description:
 * @date 2025-08-25 17:26:49
 */
@Data
public class HuiFuPayReq {

    /**
     * 交易单号
     */
    private Long tradeNo;

    /**
     * 商品描述
     */
    private String goodsInfo;

    /**
     * 交易类型
     */
    private PayEnum.HuiFuTradeType tradeType;

    /**
     * 支付金额
     */
    private Integer payAmount;
}
