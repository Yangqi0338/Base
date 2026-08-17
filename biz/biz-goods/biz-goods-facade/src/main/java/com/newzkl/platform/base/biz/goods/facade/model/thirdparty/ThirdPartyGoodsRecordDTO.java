package com.newzkl.platform.base.biz.goods.facade.model.thirdparty;

import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 第三方商品同步记录领域模型
 *
 * <p>封装与第三方平台(如会订货)进行商品同步交互的全链路信息 记录同步请求参数 响应结果与状态 供失败补偿参考</p>
 */
@Data
public class ThirdPartyGoodsRecordDTO extends BaseDTO {

    /**
     * 平台类型枚举
     */
    private ThirdPartyOrderEnum.PlatformTypeEnum platformType;

    /**
     * 外部商品(SPU)ID 第三方平台的商品唯一标识
     */
    private String outSpuId;

    /**
     * 接口名称 记录本次同步调用的动作 如 goodsEvent
     */
    private String interfaceName;

    /**
     * 请求参数JSON 第三方推送的商品数据体
     */
    private String requestJson;

    /**
     * 响应结果JSON 本系统处理结果
     */
    private String responseJson;

    /**
     * 请求状态枚举
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

    /**
     * 初始化一个新的第三方商品同步记录
     *
     * @param platformType  平台类型
     * @param outSpuId      外部商品ID
     * @param interfaceName 接口名称
     * @param requestJson   请求参数JSON
     * @param responseJson  响应结果JSON 可空
     * @param requestStatus 请求状态
     * @param errorMessage  错误信息 可空
     * @return 初始化完成的 {@link ThirdPartyGoodsRecordDTO} 实例
     */
    public static ThirdPartyGoodsRecordDTO init(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String outSpuId, String interfaceName,
                                                String requestJson, String responseJson, CommonEnum.RequestStatusEnum requestStatus, String errorMessage) {
        ThirdPartyGoodsRecordDTO request = new ThirdPartyGoodsRecordDTO();
        request.setPlatformType(platformType);
        request.setOutSpuId(outSpuId);
        request.setInterfaceName(interfaceName);
        request.setRequestJson(requestJson);
        request.setResponseJson(responseJson);
        request.setRequestStatus(requestStatus);
        request.setErrorMessage(errorMessage);
        request.setRetryCount(0);
        return request;
    }
}
