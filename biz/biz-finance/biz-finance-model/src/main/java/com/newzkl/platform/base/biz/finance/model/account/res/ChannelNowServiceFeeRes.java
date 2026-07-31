package com.newzkl.platform.base.biz.finance.model.account.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 渠道商当前服务费
 * @author niu
 * @description: 渠道商当前服务费
 * @date 2024/1/27 15:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelNowServiceFeeRes implements Serializable {

    /** 渠道商ID */
    private Long channelId;

    /**
     * 平台服务费当前服务费
     */
    private Double platformNowValue;

    /**
     * 运营商服务费当前服务费
     */
    private Double operatorNowValue;
}
