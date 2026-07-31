package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 三方下单请求记录
 *
 * @author KC
 */
@Data
public class ThirdPartyOrderRecordDTO implements Serializable {

    /**
     * 记录ID
     */
    private Long id;

    /**
     * 三方平台类型
     */
    private PlatformTypeEnum platformType;

    /**
     * 业务订单号
     */
    private String bizOrderNo;

    /**
     * 三方订单号
     */
    private String thirdOrderNo;

    /**
     * 三方接口名称
     */
    private String interfaceName;

    /**
     * 请求内容
     *
     * @ext JSON 文本
     */
    private String requestJson;

    /**
     * 响应内容
     *
     * @ext JSON 文本
     */
    private String responseJson;

    /**
     * 请求结果状态
     */
    private CommonEnum.RequestStatusEnum requestStatus;

    /**
     * 失败原因
     */
    private String errorMessage;

    /**
     * 已重试次数
     */
    private Integer retryCount;

    /**
     * 下次重试时间
     */
    private LocalDateTime nextRetryTime;
}
