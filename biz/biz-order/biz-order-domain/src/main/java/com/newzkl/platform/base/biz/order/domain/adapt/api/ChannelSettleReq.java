package com.newzkl.platform.base.biz.order.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 渠道商结算请求
 *
 * <p>迁移: 原跨域 {@code com.zkl.scm.finance.rpc.model.req.ChannelSettleReq} 降级为
 * order 本地 ACL DTO, 经 {@link BalancePayApi} 出站发起渠道商结算
 *
 * @author KC
 */
@Data
public class ChannelSettleReq implements Serializable {

    /**
     * 客户ID
     */
    private Long accountId;

    /**
     * 结算金额
     */
    private Integer settleAmount;

    /**
     * 关联结算单号
     */
    private Long joinSettleOrderNo;
}
