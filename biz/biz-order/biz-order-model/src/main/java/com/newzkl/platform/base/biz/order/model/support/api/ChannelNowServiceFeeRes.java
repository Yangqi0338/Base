package com.newzkl.platform.base.biz.order.model.support.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 渠道商当前服务费
 *
 * <p>迁移: 跨域 finance 结构 {@code com.zkl.scm.finance.model.account.res.ChannelNowServiceFeeRes}
 * 降级为 order 本地 DTO。</p>
 *
 * @author KC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelNowServiceFeeRes implements Serializable {

    /** 渠道商ID。 */
    private Long channelId;

    /** 平台服务费当前服务费。 */
    private Double platformNowValue;

    /** 运营商服务费当前服务费。 */
    private Double operatorNowValue;
}
