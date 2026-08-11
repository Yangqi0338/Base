package com.newzkl.platform.base.biz.order.domain.spi;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderDomain;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuCountDTO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import com.newzkl.platform.base.common.ddd.domain.Processor;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderDTO;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderResult;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ThirdPartyOrderProcessor extends Processor implements ThirdPartyOrderStrategy {

    /**
     * 下单动作名 落 ThirdPartyOrderRecordDO.interfaceName
     */
    private static final String INTERFACE_CREATE = "create";

    /**
     * 补偿动作名 落 ThirdPartyOrderRecordDO.interfaceName
     */
    private static final String INTERFACE_COMPENSATION = "compensation";

    private final ObjectProvider<ThirdPartyOrderStrategy> strategyProvider;

    private final ThirdPartyOrderDomain thirdPartyOrderDomain;

    public static ThirdPartyOrderProcessor find(){
        return SpringUtil.getBean(ThirdPartyOrderProcessor.class);
    }

    /**
     * 批量处理待补偿记录(供定时任务调用)
     * <p>
     * TODO[deferred D-33] 待补偿扫描未实现 依赖尚不存在的能力面 ThirdPartyOrderRepository 缺按状态+下次重试时间批量扫描 缺乐观锁状态流转(RETRYING/FAILED_PERMANENT) ThirdPartyOrderRecordDTO 缺 version/maxRetryCount 且无调用方(ThirdPartyOrderJobHandler 未迁 Base) 补齐后按指数退避重派发 compensation
     *
     * @param batchSize 单批处理条数
     */
    public void processPendingRecords(int batchSize) {
        log.warn("processPendingRecords 暂未实现 待补偿扫描能力面(批量扫描/乐观锁/重试计数)与定时任务调用方尚未迁入 batchSize={}", batchSize);
    }

    /**
     * 创建第三方订单(派发器) 按订单外部平台路由到匹配策略
     *
     * @param outGoods 外部商品列表
     * @param order    系统内部订单
     * @return 三方订单结果 非三方单或无匹配策略时返回 null
     */
    @Override
    public ThirdPartyOrderResult create(List<OrderSkuVO> outGoods, OrderDTO order) {
        PlatformTypeEnum platformType = resolvePlatformType(order, outGoods);
        if (platformType == null) {
            log.info("订单非三方单或平台来源为空 跳过三方下单派发 orderId={}", order == null ? null : order.getId());
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
            storeRecord(platformType, bizOrderNo, INTERFACE_CREATE, result, CommonEnum.RequestStatusEnum.SUCCESS, null);
            return result;
        } catch (RuntimeException e) {
            storeRecord(platformType, bizOrderNo, INTERFACE_CREATE, null, CommonEnum.RequestStatusEnum.FAILED, e.getMessage());
            throw e;
        }
    }

    /**
     * 发货通知(派发器)
     * <p>
     * TODO[deferred D-34] 发货履约未接通 入参无平台鉴别符 且 new-scm 语义中发货走 orderRepository.deliverNotify 而非策略(所有策略 delivery 均返回 null) 后续以新履约模型替换时 需据 outOrderNo 反查记录取 platformType 再派发 或回归 repository 侧履约
     *
     * @param outOrderNo         外部订单号
     * @param skuCountDTOList    发货商品数量
     * @param expressCompanyName 快递公司
     * @param expressNo          快递单号
     * @param channelId          渠道商ID
     * @return 三方结果 当前恒返回 null
     */
    @Override
    public ThirdPartyOrderResult delivery(String outOrderNo, List<SkuCountDTO> skuCountDTOList, String expressCompanyName, String expressNo, Long channelId) {
        return null;
    }

    /**
     * 补偿第三方订单(派发器) 按记录平台类型路由到匹配策略
     *
     * @param request 第三方订单记录 含 platformType
     */
    @Override
    public void compensation(ThirdPartyOrderRecordDTO request) {
        if (request == null || request.getPlatformType() == null) {
            log.warn("补偿请求为空或平台类型缺失 跳过补偿派发");
            return;
        }
        ThirdPartyOrderStrategy strategy = matchStrategy(request.getPlatformType());
        if (strategy == null) {
            log.warn("未找到匹配的三方补偿策略 platformType={} bizOrderNo={}", request.getPlatformType(), request.getBizOrderNo());
            return;
        }
        try {
            strategy.compensation(request);
            // 补偿后 策略已把三方响应/状态/错误回写进 request 追加一行补偿动作日志
            thirdPartyOrderDomain.recordAction(request.getPlatformType(), request.getBizOrderNo(), INTERFACE_COMPENSATION,
                    request.getThirdOrderNo(), request.getRequestJson(), request.getResponseJson(),
                    request.getRequestStatus() == null ? CommonEnum.RequestStatusEnum.SUCCESS : request.getRequestStatus(),
                    request.getErrorMessage());
        } catch (RuntimeException e) {
            thirdPartyOrderDomain.recordAction(request.getPlatformType(), request.getBizOrderNo(), INTERFACE_COMPENSATION,
                    request.getThirdOrderNo(), request.getRequestJson(), request.getResponseJson(),
                    CommonEnum.RequestStatusEnum.FAILED, e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean supports(Object type) {
        // 派发器本身不参与业务 恒 false 使 strategyProvider 过滤时自动排除自身
        return false;
    }

    /**
     * 解析订单外部平台 优先取订单级 platformType 缺失时回退到 outGoods 首个非空商品级 platformType
     */
    private PlatformTypeEnum resolvePlatformType(OrderDTO order, List<OrderSkuVO> outGoods) {
        if (order != null && order.getPlatformType() != null) {
            return order.getPlatformType();
        }
        if (CollUtil.isNotEmpty(outGoods)) {
            return outGoods.stream()
                    .map(OrderSkuVO::getPlatformType)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * 从 strategyProvider 中选出 supports(platformType) 命中的首个策略
     */
    private ThirdPartyOrderStrategy matchStrategy(PlatformTypeEnum platformType) {
        return strategyProvider.stream()
                .filter(s -> s.supports(platformType))
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
    private void storeRecord(PlatformTypeEnum platformType, String bizOrderNo, String interfaceName,
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
