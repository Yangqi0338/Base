package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;

import com.newzkl.platform.base.biz.finance.model.pay.res.TradeBaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.finance.HuifuEnum;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HuiFuPayRes extends TradeBaseRes implements PayBaseResult {

    /**
     * 支付类型
     */
    private HuifuEnum.HuiFuTradeType tradeType;

    /**
     * 支付二维码
     */
    private String qrCode;

    /**
     *
     */
    private String payInfo;

}
