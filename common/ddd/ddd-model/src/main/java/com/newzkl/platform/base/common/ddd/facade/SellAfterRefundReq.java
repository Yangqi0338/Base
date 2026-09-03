package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 售后退款请求 (facade 自带 model, 防腐: 金额 Integer 分)。
 *
 * @author KC
 */
@Data
public class SellAfterRefundReq implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 退款金额 (分)
     */
    private Money refundAmount;

    /**
     * 售后单号
     */
    private Long sellAfterOrderNo;

    /**
     * 服务费 (分)
     */
    private Money serviceAmount;

    /**
     * 订单主键(资金流水 join_record_id 关联键, 沿用 id 口径)
     */
    private Long orderId;

    /**
     * 变动类型码
     */
    private Integer alterType;

    /**
     * 备注
     */
    private Integer remark;
}
