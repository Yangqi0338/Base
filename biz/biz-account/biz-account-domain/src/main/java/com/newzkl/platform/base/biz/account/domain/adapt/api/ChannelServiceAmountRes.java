package com.newzkl.platform.base.biz.account.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 渠道商当前服务费结果。
 *
 * <p>迁移: 跨域 finance 结构
 * {@code com.zkl.scm.finance.rpc.model.account.res.ChannelServiceAmountRes}
 * 降级为 account 本地端口 DTO。</p>
 *
 * @author KC
 */
@Data
public class ChannelServiceAmountRes implements Serializable {

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 平台阶梯配置 (JSON)
     */
    private String platformConfig;

    /**
     * 平台当前服务费率
     */
    private Double platformNowValue;

    /**
     * 变更时间
     */
    private LocalDateTime alterTime;
}
