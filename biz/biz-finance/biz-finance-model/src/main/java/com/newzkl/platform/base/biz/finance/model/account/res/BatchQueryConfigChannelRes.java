package com.newzkl.platform.base.biz.finance.model.account.res;

import lombok.Data;

/**
 * @author niu
 * @description:
 * @date 2024/3/15 17:18
 */
@Data
public class BatchQueryConfigChannelRes {

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 运营商服务费配置
     */
    private String operatorConfig;

    /**
     * 运营商服务费当前服务费
     */
    private Double operatorNowValue;

    /**
     * 采购金账户余额
     */
    private Integer amount;
}
