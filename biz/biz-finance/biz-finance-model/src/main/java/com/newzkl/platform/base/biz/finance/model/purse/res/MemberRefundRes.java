package com.newzkl.platform.base.biz.finance.model.purse.res;

import com.newzkl.platform.base.biz.finance.model.pay.res.PayBaseRes;
import lombok.Data;

@Data
public class MemberRefundRes implements PayBaseRes {

    /**
     * 退款警告信息
     */
    private String refundWarnMsg;

    /**
     * 退款单号
     */
    private Long refundNo;

    /**
     * 三方退款单号
     */
    private String thirdTradeNo;

}
