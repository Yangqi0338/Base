package com.newzkl.platform.base.biz.finance.model.account.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 渠道商服务费VO
 * @date 2024/1/22 15:06
 */
@Data
public class ConfigChannelVO {

    /** 渠道商ID */
    private Long channelId;

    /**
     * 平台服务费配置
     */
    private String platformConfig;

    /**
     * 运营商服务费配置
     */
    private String operatorConfig;

    /**
     * 平台服务费当前服务费
     */
    private Double platformNowValue;

    /**
     * 运营商服务费当前服务费
     */
    private Double operatorNowValue;

    /**
     * 更新时间
     */
    private LocalDateTime alterTime;
}
