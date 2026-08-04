package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 门店客户支付入参
 *
 * @author KC
 */
@Data
public class StoreAccountPayCommand implements Serializable {

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 客户账户ID
     */
    private Long accountId;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 支付金额
     *
     * @ext 单位分
     */
    private Money payAmount;
}
