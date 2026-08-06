package com.newzkl.platform.base.biz.order.facade.model.order;



import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 第三方订单请求领域模型
 * <p>
 * 该类封装了与第三方平台（如YYT、惠订货）进行订单交互的全链路信息。 它不仅记录了请求的基本参数和响应结果，还包含了用于失败补偿机制的状态和重试策略信息。 是连接业务层、数据访问层以及补偿任务的核心数据载体。
 * @author sijiwang
 */
@Data
public class ThirdPartyOrderRecordDTO extends BaseDTO {
    
    /**
     * 平台类型枚举
     */
    private PlatformTypeEnum platformType;
    
    /**
     * 业务订单号 系统内部生成的唯一订单标识，用于追踪和关联订单在本系统中的所有操作。
     */
    private String bizOrderNo;
    
    /**
     * 第三方订单号 第三方平台成功创建订单后返回的唯一订单号。 在请求成功后被填充。
     */
    private String thirdOrderNo;
    
    /**
     * 接口名称 记录本次请求调用的第三方平台的具体接口，例如 "createOrder"、"cancelOrder" 等。
     */
    private String interfaceName;
    
    /**
     * 请求参数JSON 调用第三方接口时，发送的请求体（Request Body）内容，以JSON字符串形式存储。
     */
    private String requestJson;
    
    /**
     * 响应结果JSON 第三方接口返回的响应体（Response Body）内容，以JSON字符串形式存储。 在请求成功或失败时都可能被填充，用于问题排查。
     */
    private String responseJson;
    
    /**
     * 请求状态枚举 标识当前请求的最终状态，如：
     */
    private CommonEnum.RequestStatusEnum requestStatus;
    
    /**
     * 错误信息 当请求失败
     */
    private String errorMessage;
    
    /**
     * 重试次数 记录当前请求已经进行过的补偿重试次数。
     */
    private Integer retryCount;
    
    /**
     * 下次重试时间 记录下一次补偿任务应该何时尝试重新执行此请求。 通常采用指数退避策略（Exponential Backoff）来计算。
     */
    private LocalDateTime nextRetryTime;

    /**
     * 初始化一个新的第三方订单请求记录。
     * <p>
     * 这是一个静态工厂方法，用于在创建新请求时提供一个统一的、状态一致的实例。 它会设置一些默认值，如重试次数为0，并初始化创建和更新时间。
     *
     * @param platformType 平台类型
     * @param bizOrderNo 业务订单号
     * @param interfaceName 接口名称
     * @param requestJson 请求参数JSON
     * @param responseJson 响应结果JSON (初始创建时可能为null)
     * @param errorMessage 错误信息 (初始创建时可能为null)
     * @return 一个初始化完成的 {@link ThirdPartyOrderRecordDTO} 实例
     */
    public static ThirdPartyOrderRecordDTO init(PlatformTypeEnum platformType, String bizOrderNo, String interfaceName,
                                                String requestJson, String responseJson, CommonEnum.RequestStatusEnum requestStatus, String errorMessage) {
        ThirdPartyOrderRecordDTO request = new ThirdPartyOrderRecordDTO();
        request.setPlatformType(platformType);
        request.setBizOrderNo(bizOrderNo);
        request.setInterfaceName(interfaceName);
        request.setRequestJson(requestJson);
        request.setResponseJson(responseJson);
        request.setRequestStatus(requestStatus);
        request.setErrorMessage(errorMessage);
        request.setRetryCount(0); // 初始重试次数为0
        return request;
    }
}