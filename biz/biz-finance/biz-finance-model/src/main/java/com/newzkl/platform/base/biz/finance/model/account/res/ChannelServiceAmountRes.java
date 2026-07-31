package com.newzkl.platform.base.biz.finance.model.account.res;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 渠道商服务费VO
 * @date 2024/1/22 15:06
 */
@Data
public class ChannelServiceAmountRes implements Serializable {

    /** 渠道商ID */
    private Long channelId;

    /**
     * 平台服务费配置
     */
    private String platformConfig;

    /**
     * 平台服务费当前服务费
     */
    private Double platformNowValue;

    /**
     * 更新时间
     */
    private LocalDateTime alterTime;
}
