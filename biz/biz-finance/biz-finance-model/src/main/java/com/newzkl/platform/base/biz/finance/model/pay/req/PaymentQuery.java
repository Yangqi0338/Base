package com.newzkl.platform.base.biz.finance.model.pay.req;


import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 支付单
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentQuery extends PageQuery {
    /**
     * 交易单号
     */
    private List<Long> tradeNoList;

    /**
     * 交易单号
     */
    private List<Long> orderNoList;

    /**
     * {@link EarningsEnum }
     * 1: "礼包" ,2, "渠道商充值" ,3: "商品" ,4: "兑换码" ,5: "供应商运营账户充值" ,6: "分红"
     * <p>
     * 消费类型
     */
    private String consumeType;


    /**
     * 支付状态
     */
    private Integer payState;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 充值档位
     */
    private Integer level;
}