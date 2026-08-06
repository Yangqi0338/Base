package com.newzkl.platform.base.biz.order.model.support.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 分润配置跨域 ACL 视图
 *
 * <p>迁移: 跨域 message 结构 {@code com.zkl.scm.message.rpc.model.user.EarningsConfigRpcVO}
 * 降级为 order 本地 ACL DTO。经 {@code OrderRepository#channelEarningsConfig} 出站获取,
 * 供下单编排读取渠道商上级链(运营商/交易师)与费率</p>
 *
 * @author KC
 */
@Data
public class EarningsConfigRpcVO implements Serializable {

    /** 被分润用户ID(渠道商ID)。 */
    private Long id;

    /** 上级运营商ID。 */
    private Long upOperatorId;

    /** 上级交易师ID。 */
    private Long upDealerId;

    /** 交易师费率。 */
    private Double dealerRate;

    /** 供应商ID。 */
    private Long supplierId;
}
