package com.newzkl.platform.base.biz.finance.model.purse.req;


import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 售后退款请求
 *
 * @author niu
 * @date 2024/1/26 17:55
 */
@Data
public class SellAfterRefundReq implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 退款金额
     */
    private Money refundAmount;

    /**
     * 售后单号
     */
    private Long sellAfterOrderNo;

    /**
     * 服务费
     */
    private Money serviceAmount;

    /**
     * 订单单号
     */
    private Long orderNo;

    /**
     * 账户变动类型
     */
    private PurseEnum.AlterType alterType = PurseEnum.AlterType.SELL_AFTER;
}
