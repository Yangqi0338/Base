package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;

import com.newzkl.platform.base.biz.finance.model.pay.res.TradeBaseRes;
import lombok.Data;

@Data
public class HuiFuRefundRes extends TradeBaseRes {

    /**
     * 交易请求日期 yyyyMMdd
     */
    private String tradeDate;

    /**
     * 实际退款金额
     */
    private Double actualRefundAmount;

}
