package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.mybatis.handler.RawJsonStringTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

/**
 * 渠道商服务费配置 DO
 *
 * @author kc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap=true)
public class ConfigChannelDO extends BaseDO {
    /**
     * 渠道商id
     */
    @Index
    private Long channelId;

    /**
     * 平台服务费
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String platformConfig;

    /**
     * 运营商服务费
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
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