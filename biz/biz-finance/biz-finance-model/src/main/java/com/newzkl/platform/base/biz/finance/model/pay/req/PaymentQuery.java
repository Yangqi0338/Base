package com.newzkl.platform.base.biz.finance.model.pay.req;


import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.model.auth.OauthUserId;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 支付单
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentQuery extends BizPageQuery {
    /**
     * 交易单号
     */
    private List<String> tradeNoList;

    /**
     * 交易单号
     */
    private List<Long> orderNoList;

    public void setOrderNo(Long orderNo) {
        this.orderNoList = doWrapperList(this.orderNoList, orderNo);
    }

    /**
     * 消费类型
     */
    private EarningsEnum.ConsumeType consumeType;


    /**
     * 支付状态
     */
    private PaymentEnum.PayState payState;

    /**
     * 客户id
     */
    @OauthUserId
    private Long accountId;

    /**
     * 充值档位
     */
    private Integer level;
}