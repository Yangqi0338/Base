package com.newzkl.platform.base.common.ddd.facade;

import lombok.Data;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * 渠道商充值服务费配置入参
 *
 * <p>迁移: 跨域 finance 结构
 * {@code com.zkl.scm.finance.rpc.model.account.req.ChargeConfigChannelReq}
 * 降级为 account 本地端口 DTO。</p>
 *
 * @author KC
 */
@Data
public class ChargeConfigChannelReq implements Serializable {

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 充值金额 (分)
     */
    private Integer rechargeAmount;

    /**
     * 平台当前服务费率
     */
    private Double platformNowValue;

    /**
     * 平台阶梯配置: 金额 -> 费率
     */
    private TreeMap<Integer, Double> platformConfig;
}
