package com.newzkl.platform.base.biz.finance.model.account.req;

import lombok.Data;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * @author niu
 * @description: 渠道商服务费配置
 * @date 2024/1/22 14:51
 */
@Data
public class ChargeConfigChannelReq implements Serializable {

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 充值金额
     */
    private Integer rechargeAmount;

    /**
     * 平台当前服务费
     */
    private Double platformNowValue;

    /**
     * 运营商当前服务费
     */
    private Double operatorNowValue;

    /**
     * 平台服务费
     */
    private TreeMap<Integer, Double> platformConfig;

    /**
     * 运营商服务费
     */
    private TreeMap<Integer, Double> operatorConfig;


}
