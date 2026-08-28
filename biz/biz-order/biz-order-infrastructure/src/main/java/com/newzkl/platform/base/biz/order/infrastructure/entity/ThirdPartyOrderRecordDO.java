package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.mybatis.handler.RawJsonStringTypeHandler;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;

/**
 * 第三方订单请求数据对象 (DO)
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class ThirdPartyOrderRecordDO extends BaseDO {

    /**
     * 平台类型
     */
    private ThirdPartyOrderEnum.PlatformTypeEnum platformType;

    /**
     * 业务订单号
     */
    @Index
    private String bizOrderNo;

    /**
     * 第三方订单号
     */
    @Index
    private String thirdOrderNo;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 请求参数
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String requestJson;

    /**
     * 响应结果
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String responseJson;

    /**
     * 请求状态
     */
    private CommonEnum.RequestStatusEnum requestStatus;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 下次重试时间
     */
    private LocalDateTime nextRetryTime;
}