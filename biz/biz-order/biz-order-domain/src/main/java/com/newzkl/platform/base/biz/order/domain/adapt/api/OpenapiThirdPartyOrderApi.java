package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.biz.order.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.PlatformTypeEnum;

/**
 * 三方下单请求记录出站端口
 *
 * @author KC
 */
public interface OpenapiThirdPartyOrderApi {

    /**
     * 登记一次三方下单请求
     *
     * @param platformType   三方平台类型
     * @param bizOrderNo     业务订单号
     * @param interfaceName  三方接口名称
     * @param requestObject  请求内容
     * @param responseObject 响应内容
     * @param requestStatus  请求结果状态
     * @param errorMessage   失败原因, 成功时可为空
     * @return 登记后的请求记录, 未接入外部实现时为 null
     */
    ThirdPartyOrderRecordDTO createRequestRecord(PlatformTypeEnum platformType, String bizOrderNo,
                                                String interfaceName, Object requestObject, Object responseObject,
                                                CommonEnum.RequestStatusEnum requestStatus, String errorMessage);
}
