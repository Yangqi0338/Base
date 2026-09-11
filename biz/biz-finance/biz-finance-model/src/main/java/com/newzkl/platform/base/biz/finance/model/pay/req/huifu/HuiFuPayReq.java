package com.newzkl.platform.base.biz.finance.model.pay.req.huifu;

import com.newzkl.platform.base.common.ddd.model.enums.finance.HuifuEnum;
import lombok.Data;

/**
 * 汇付支付请求
 *
 * @author niu
 * @date 2025-08-25 17:26:49
 */
@Data
public class HuiFuPayReq {

    /**
     * 交易单号
     */
    private String tradeNo;

    /**
     * 商品描述
     */
    private String goodsInfo;

    /**
     * 交易类型
     */
    private HuifuEnum.HuiFuTradeType tradeType;

    /**
     * 支付金额
     */
    private Integer payAmount;

    /**
     * 微信用户openId
     */
    private String wxOpenId;
}
