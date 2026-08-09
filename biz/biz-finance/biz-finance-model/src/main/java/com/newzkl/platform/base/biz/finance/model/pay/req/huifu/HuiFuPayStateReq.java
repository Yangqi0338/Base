package com.newzkl.platform.base.biz.finance.model.pay.req.huifu;

import lombok.Data;

/**
 * 汇付检查支付状态请求
 *
 * @author niu
 * @date 2025-08-25 17:26:49
 */
@Data
public class HuiFuPayStateReq {

    /**
     * 汇付交易号
     */
    private String huifuTradeNo;

}
