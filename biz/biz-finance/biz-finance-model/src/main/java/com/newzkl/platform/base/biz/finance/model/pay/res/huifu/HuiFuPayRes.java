package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;

import com.newzkl.platform.base.biz.finance.model.pay.res.TradeBaseRes;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PayEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HuiFuPayRes extends TradeBaseRes implements PayBaseResult {

    /**
     * 支付类型
     */
    private PayEnum.HuiFuTradeType tradeType;

    /**
     * 支付二维码
     */
    private String qrCode;

}
