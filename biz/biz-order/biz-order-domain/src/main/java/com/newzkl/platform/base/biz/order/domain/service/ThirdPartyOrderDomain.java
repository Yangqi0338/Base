package com.newzkl.platform.base.biz.order.domain.service;


import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;

public interface ThirdPartyOrderDomain {

    ThirdPartyOrderRecordDTO createRecord(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String bizOrderNo, String interfaceName, Object requestObject, Object responseObject, CommonEnum.RequestStatusEnum requestStatus, String errorMessage);

    /**
     * 记录一次三方动作(下单/补偿等) 追加式动作日志 每次调用落一行 不做按业务订单号去重
     *
     * <p>与 {@link #createRecord} 的区别 requestJson/responseJson 为调用方已序列化的 JSON 文本 直接落库不再二次序列化
     * 供 ThirdPartyOrderProcessor 派发器在动作前后统一存储 记录失败仅记日志不抛出 不影响主流程</p>
     *
     * @param platformType  平台类型
     * @param bizOrderNo    业务订单号
     * @param interfaceName 接口/动作名 如 create compensation
     * @param thirdOrderNo  三方订单号 可空
     * @param requestJson   请求 JSON 文本
     * @param responseJson  响应 JSON 文本 可空
     * @param requestStatus 请求状态
     * @param errorMessage  错误信息 可空
     */
    void recordAction(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String bizOrderNo, String interfaceName, String thirdOrderNo,
                      String requestJson, String responseJson, CommonEnum.RequestStatusEnum requestStatus, String errorMessage);
}
