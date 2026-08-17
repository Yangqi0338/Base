package com.newzkl.platform.base.biz.goods.domain.service;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;

public interface ThirdPartyGoodsDomain {

    /**
     * 记录一次三方商品同步动作 追加式动作日志 每次调用落一行 不做去重
     *
     * <p>requestJson/responseJson 为调用方已序列化的 JSON 文本 直接落库不再二次序列化</p>
     *
     * @param platformType  平台类型
     * @param outSpuId      外部商品ID
     * @param interfaceName 接口/动作名 如 goodsEvent
     * @param requestJson   请求 JSON 文本
     * @param responseJson  响应 JSON 文本 可空
     * @param requestStatus 请求状态
     * @param errorMessage  错误信息 可空
     */
    void recordAction(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String outSpuId, String interfaceName,
                      String requestJson, String responseJson, CommonEnum.RequestStatusEnum requestStatus, String errorMessage);
}
