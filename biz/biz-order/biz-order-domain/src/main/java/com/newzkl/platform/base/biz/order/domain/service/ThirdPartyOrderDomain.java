package com.newzkl.platform.base.biz.order.domain.service;


import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;

/**
 * 三方订单记录领域服务
 *
 * @author fang
 */
public interface ThirdPartyOrderDomain {

    /**
     * 记录一次三方动作(下单/补偿/通知等) 追加式动作日志 每次调用落一行 不做按业务订单号去重
     *
     * <p>requestJson/responseJson 为调用方已序列化的 JSON 文本 直接落库不再二次序列化
     * 供 ThirdPartyOrderProcessor 派发器与开发者通知消费者在动作前后统一存储 记录失败仅记日志不抛出 不影响主流程</p>
     *
     * <p>仅当 requestStatus 为失败且 interfaceName 为 {@link ThirdPartyOrderEnum.Action#NOTIFY} 时排下一次补偿时间
     * 其余动作(下单/补偿日志)不排期 避免补偿任务重推下单报文或让审计行自我增殖</p>
     *
     * @param platformType  平台类型
     * @param bizOrderNo    业务订单号
     * @param interfaceName 接口/动作名 取值见 {@link ThirdPartyOrderEnum.Action}
     * @param thirdOrderNo  三方订单号 可空
     * @param requestJson   请求 JSON 文本
     * @param responseJson  响应 JSON 文本 可空
     * @param requestStatus 请求状态
     * @param errorMessage  错误信息 可空
     */
    void recordAction(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String bizOrderNo, String interfaceName, String thirdOrderNo,
                      String requestJson, String responseJson, CommonEnum.RequestStatusEnum requestStatus, String errorMessage);

    /**
     * 按 id 更新一条三方订单记录 用于补偿后回写状态与重试计数
     *
     * @param record 带 id 的记录 无 id 会退化成插入
     * @return 更新后的记录
     */
    ThirdPartyOrderRecordDTO updateRecord(ThirdPartyOrderRecordDTO record);
}
