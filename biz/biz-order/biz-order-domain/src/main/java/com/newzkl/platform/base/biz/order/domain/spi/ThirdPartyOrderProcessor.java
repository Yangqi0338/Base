package com.newzkl.platform.base.biz.order.domain.spi;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderDomain;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import com.newzkl.platform.base.common.ddd.domain.Processor;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderResult;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 三方派发器 下单轴与补偿轴各走各的 Provider
 *
 * <p>本类不实现任何 SPI 故不会被自身的 {@code strategyProvider} 收进候选集</p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ThirdPartyOrderProcessor extends Processor {

    /**
     * 下单轴(轴B 供货平台) 候选策略
     */
    private final ObjectProvider<ThirdPartyOrderStrategy> strategyProvider;

    /**
     * 补偿轴(轴C 通知重推) 候选补偿器
     */
    private final ObjectProvider<ThirdPartyCompensator> compensatorProvider;

    private final ThirdPartyOrderDomain thirdPartyOrderDomain;

    public static ThirdPartyOrderProcessor find(){
        return SpringUtil.getBean(ThirdPartyOrderProcessor.class);
    }

    /**
     * 创建第三方订单(派发器) 按商品级供货平台路由到匹配策略
     *
     * @param outGoods 外部商品列表
     * @param order    系统内部订单
     * @return 三方订单结果 非三方单或无匹配策略时返回 null
     */
    public ThirdPartyOrderResult create(List<OrderSkuVO> outGoods, OrderDTO order) {
        ThirdPartyOrderEnum.PlatformTypeEnum platformType = resolvePlatformType(outGoods);
        if (platformType == null) {
            log.info("订单无三方供货商品 跳过三方下单派发 orderId={}", order == null ? null : order.getId());
            return null;
        }
        ThirdPartyOrderStrategy strategy = matchStrategy(platformType);
        if (strategy == null) {
            log.warn("未找到匹配的三方下单策略 platformType={} orderId={}", platformType, order == null ? null : order.getId());
            return null;
        }
        String bizOrderNo = order == null || order.getId() == null ? null : String.valueOf(order.getId());
        try {
            ThirdPartyOrderResult result = strategy.create(outGoods, order);
            storeRecord(platformType, bizOrderNo, ThirdPartyOrderEnum.Action.CREATE, result, CommonEnum.RequestStatusEnum.SUCCESS, null);
            return result;
        } catch (RuntimeException e) {
            storeRecord(platformType, bizOrderNo, ThirdPartyOrderEnum.Action.CREATE, null, CommonEnum.RequestStatusEnum.FAILED, e.getMessage());
            throw e;
        }
    }

    /**
     * 补偿第三方订单(派发器) 按记录平台类型路由到匹配补偿器 派发后回写状态与重试计数
     *
     * @param request 第三方订单记录 含 platformType 与 id
     */
    public void compensation(ThirdPartyOrderRecordDTO request) {
        if (request == null || request.getPlatformType() == null) {
            log.warn("补偿请求为空或平台类型缺失 跳过补偿派发");
            return;
        }
        ThirdPartyCompensator compensator = matchCompensator(request.getPlatformType());
        if (compensator == null) {
            log.warn("未找到匹配的三方补偿器 platformType={} bizOrderNo={}", request.getPlatformType(), request.getBizOrderNo());
            return;
        }
        try {
            compensator.compensation(request);
        } catch (RuntimeException e) {
            request.setRequestStatus(CommonEnum.RequestStatusEnum.FAILED);
            request.setErrorMessage(e.getMessage());
            log.error("补偿派发异常 platformType={} bizOrderNo={}", request.getPlatformType(), request.getBizOrderNo(), e);
        }
        // 追加一行补偿动作日志(审计 恒 INSERT) 与下面按 id 回写原记录是两条独立路径
        thirdPartyOrderDomain.recordAction(request.getPlatformType(), request.getBizOrderNo(), ThirdPartyOrderEnum.Action.COMPENSATION,
                request.getThirdOrderNo(), request.getRequestJson(), request.getResponseJson(),
                request.getRequestStatus() == null ? CommonEnum.RequestStatusEnum.SUCCESS : request.getRequestStatus(),
                request.getErrorMessage());
        writeBackRetry(request);
    }

    /**
     * 回写原记录的补偿结果 成功即终结 失败则推进重试计数并排下一次
     *
     * @param request 带 id 的原始记录
     */
    private void writeBackRetry(ThirdPartyOrderRecordDTO request) {
        if (CommonEnum.RequestStatusEnum.SUCCESS == request.getRequestStatus()) {
            // 成功后 job 的 requestStatus=FAILED 闸门自动排除本行 无需清 nextRetryTime
            thirdPartyOrderDomain.updateRecord(request);
            return;
        }
        request.setRequestStatus(CommonEnum.RequestStatusEnum.FAILED);
        request.setRetryCount((request.getRetryCount() == null ? 0 : request.getRetryCount()) + 1);
        if (request.getRetryCount() < ThirdPartyOrderRecordDTO.MAX_RETRY_COUNT) {
            request.scheduleNextRetry();
        } else {
            log.warn("补偿重试次数耗尽 不再排期 bizOrderNo={} retryCount={}", request.getBizOrderNo(), request.getRetryCount());
        }
        thirdPartyOrderDomain.updateRecord(request);
    }

    /**
     * 解析供货平台(轴B) 只认商品级 platformType
     * <p>订单级 {@code OrderDTO.platformType} 是订单来源(轴A 如 openapi 单恒为 LE_TAI) 与由谁供货无关
     * 若参与派发会遮蔽商品级的会订货代发 故此处不读订单级</p>
     */
    private ThirdPartyOrderEnum.PlatformTypeEnum resolvePlatformType(List<OrderSkuVO> outGoods) {
        if (CollUtil.isEmpty(outGoods)) {
            return null;
        }
        return outGoods.stream()
                .map(OrderSkuVO::getPlatformType)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * 从 strategyProvider 中选出 supports(platformType) 命中的首个策略
     */
    private ThirdPartyOrderStrategy matchStrategy(ThirdPartyOrderEnum.PlatformTypeEnum platformType) {
        return strategyProvider.stream()
                .filter(s -> s.supports(platformType))
                .findFirst()
                .orElse(null);
    }

    /**
     * 从 compensatorProvider 中选出 supports(platformType) 命中的首个补偿器
     */
    private ThirdPartyCompensator matchCompensator(ThirdPartyOrderEnum.PlatformTypeEnum platformType) {
        return compensatorProvider.stream()
                .filter(c -> c.supports(platformType))
                .findFirst()
                .orElse(null);
    }

    /**
     * 统一存储一次派发动作 从 ThirdPartyOrderResult 取已序列化 req/res 落追加式动作日志
     *
     * <p>存储失败仅记日志不抛出 不影响三方下单主流程 Task D 引入 ThirdPartyGoodsProcessor 时上提到 AbstractThirdPartyProcessor 基类复用</p>
     *
     * @param platformType  平台类型
     * @param bizOrderNo    业务订单号
     * @param interfaceName 动作名
     * @param result        三方结果 成功时非空 失败时为 null
     * @param status        请求状态
     * @param errorMessage  错误信息 可空
     */
    private void storeRecord(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String bizOrderNo, String interfaceName,
                             ThirdPartyOrderResult result, CommonEnum.RequestStatusEnum status, String errorMessage) {
        try {
            String requestJson = result == null ? null : result.getOrderReq();
            String responseJson = result == null ? null : result.getOrderRes();
            String thirdOrderNo = result == null ? null : result.getOrderSn();
            thirdPartyOrderDomain.recordAction(platformType, bizOrderNo, interfaceName, thirdOrderNo,
                    requestJson, responseJson, status, errorMessage);
        } catch (Exception ex) {
            log.error("存储三方动作记录失败 platformType={} bizOrderNo={} interfaceName={}", platformType, bizOrderNo, interfaceName, ex);
        }
    }
}
