package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * @author 渠道商服务费配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class ConfigChannelDO extends BaseDO {
    /**
     * 渠道商id
     */
    @Index
    private Long channelId;

    /**
     * 平台服务费
     */
    @JsonSerialize
    private String platformConfig;

    /**
     * 运营商服务费
     */
    @JsonSerialize
    private String operatorConfig;

    /**
     * 平台当前服务费
     */
    private Double platformNowValue;

    /**
     * 运营商当前服务费
     */
    private Double operatorNowValue;
}