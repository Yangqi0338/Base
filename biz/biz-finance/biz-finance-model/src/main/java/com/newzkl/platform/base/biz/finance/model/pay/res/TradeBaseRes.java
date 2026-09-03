package com.newzkl.platform.base.biz.finance.model.pay.res;

import cn.hutool.core.date.DateUtil;
import com.newzkl.platform.base.biz.finance.model.support.TripartiteBaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class TradeBaseRes extends TripartiteBaseRes {

    /**
     * 单号
     */
    private String tradeNo;

    /**
     * 金额
     */
    private Double tradeAmount;

    /**
     * 完成时间 yyyyMMddHHmmss
     */
    private String tradeFinishTime;

    public String getTradeFinishTime() {
        if (tradeFinishTime == null) {
            return DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        }
        return tradeFinishTime;
    }
}