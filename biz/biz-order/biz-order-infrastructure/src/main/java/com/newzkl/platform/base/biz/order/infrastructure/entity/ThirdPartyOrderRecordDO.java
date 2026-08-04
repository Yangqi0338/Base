package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 第三方订单请求数据对象 (DO)
 * @author sijiwang
 */
@Data
public class ThirdPartyOrderRecordDO extends BaseDO {

    /**
     * 平台类型 (对应数据库表 `third_party_order_request.platform_type`)
     */
    private PlatformTypeEnum platformType;

    /**
     * 业务订单号 (对应数据库表 `third_party_order_request.biz_order_no`)
     */
    private String bizOrderNo;

    /**
     * 第三方订单号 (对应数据库表 `third_party_order_request.third_order_no`)
     */
    private String thirdOrderNo;

    /**
     * 接口名称 (对应数据库表 `third_party_order_request.interface_name`)
     */
    private String interfaceName;

    /**
     * 请求参数JSON (对应数据库表 `third_party_order_request.request_json`)
     */
    private String requestJson;

    /**
     * 响应结果JSON (对应数据库表 `third_party_order_request.response_json`)
     */
    private String responseJson;

    /**
     * 请求状态 (对应数据库表 `third_party_order_request.request_status`)
     */
    private Integer requestStatus;

    /**
     * 错误信息 (对应数据库表 `third_party_order_request.error_message`)
     */
    private String errorMessage;

    /**
     * 重试次数 (对应数据库表 `third_party_order_request.retry_count`)
     */
    private Integer retryCount;

    /**
     * 下次重试时间 (对应数据库表 `third_party_order_request.next_retry_time`)
     */
    private LocalDateTime nextRetryTime;

    /**
     * 创建时间 (对应数据库表 `third_party_order_request.created_at`)
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间 (对应数据库表 `third_party_order_request.updated_at`)
     */
    private LocalDateTime updatedAt;
}