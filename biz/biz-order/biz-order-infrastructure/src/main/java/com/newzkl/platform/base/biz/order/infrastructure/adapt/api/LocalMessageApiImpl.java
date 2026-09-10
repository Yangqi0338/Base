package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import cn.hutool.core.util.ObjectUtil;
import com.newzkl.platform.base.biz.order.domain.adapt.api.LocalMessageApi;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateRecordRPC;
import com.newzkl.platform.base.biz.order.facade.model.order.RefundOperationRecordRPC;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.event.OrderDeliveryEvent;
import com.newzkl.platform.base.biz.order.model.event.OrderStateEvent;
import com.newzkl.platform.base.biz.order.model.event.RefundStateEvent;
import com.newzkl.platform.base.biz.order.model.support.api.order.RefundPassEvent;
import com.newzkl.platform.base.common.core.mq.domain.LocalMessageRepository;
import com.newzkl.platform.base.common.core.mq.infrastructure.utils.MQUtil;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * {@code LocalMessageApi} 的基础设施实现
 *
 * <p>发送类方法直接经 {@code MQUtil} 投递(内部落本地消息表 + MQ), 唤醒类方法经
 * {@code LocalMessageRepository} 翻转 outKey 可消费标识。对等旧
 * {@code @DubboReference ILocalMessageFacade}: 单体现态同上下文, 端口注入即可,
 * 拆服务时改为远程 consumer, 上层零改动。</p>
 *
 * @author KC
 */
@Slf4j
@Component("orderLocalMessageApi")
@RequiredArgsConstructor
public class LocalMessageApiImpl implements LocalMessageApi {

    @Override
    public void sendMessage(String tag, Object messageContent, String messageClass) {
        MQUtil.send(tag, messageContent);
    }

    @Override
    public void sendRefundPassMessage(RefundDTO refund) {
        RefundPassEvent refundPassEvent = TransferUtils.transfer(refund, RefundPassEvent.class);
        // RefundDTO 关联键已改 String orderNo, 与事件的 Long orderId 不同名故不会自动映射;
        // 下游 BI 事实表(FactRefundDO/FactOrderDO/FactPaymentDO)统一按订单主键 Long 建外键,
        // 层折叠后 spuOrderId 即 order 主键(RefundDTO:228 setSpuOrderId(orderVO.getId())), 直接取用
        refundPassEvent.setOrderId(refund.getSpuOrderId());
        MQUtil.send(MQ.Tag.REFUND_PASS, refundPassEvent);
    }

    @Override
    public void wakeUpEarningMessage(Long skuOrderId) {
        MQUtil.wakeUp(MQ.Tag.SETTLE + skuOrderId);
    }

    @Override
    public void sendOrderNewRecordEvent(List<OrderDTO> orderList, OrderEnum.State beforeOrderState, OrderEnum.State afterOrderState, Long operatorId, AccountEnum.Identity operatorRoleId) {
        orderList.forEach(order -> {
            OrderStateRecordRPC orderStateRecordRPC = new OrderStateRecordRPC();
            orderStateRecordRPC.setOrderNo(order.getOrderNo());
            // SpuOrder 层折叠: spu_order_id 列名保留(归 slug26), 值同 orderId
            orderStateRecordRPC.setSpuOrderId(order.getId());
            orderStateRecordRPC.setBeforeOrderState(beforeOrderState);
            orderStateRecordRPC.setBeforeStateDesc(beforeOrderState.getValue());
            orderStateRecordRPC.setAfterOrderState(afterOrderState);
            orderStateRecordRPC.setAfterStateDesc(afterOrderState.getValue());
            orderStateRecordRPC.setOrdererId(order.getAccountId());
            orderStateRecordRPC.setOperatorId(operatorId);
            orderStateRecordRPC.setOperatorRoleId(operatorRoleId);
            orderStateRecordRPC.setRoleDesc(operatorRoleId.getValue());
            orderStateRecordRPC.setOperateTime(LocalDateTime.now());
            orderStateRecordRPC.setCreateTime(LocalDateTime.now());
            orderStateRecordRPC.setUpdateTime(LocalDateTime.now());
            MQUtil.send(MQ.Tag.ORDER_STATE_RECORD_EVENT,orderStateRecordRPC);
            log.info("订单状态记录消息发送成功，orderStateRecordRPC: {}", orderStateRecordRPC);
        });
    }

    @Override
    public void publishOrderState(OrderEnum.OrderType orderType, Long channelId, String outOrderNo, OrderEnum.State sourceState, OrderEnum.State newState) {
        MQUtil.send(MQ.Tag.ORDER_STATE_EVENT, new OrderStateEvent(outOrderNo, channelId, orderType, sourceState, newState));
    }

    @Override
    public void publishRefundState(OrderEnum.OrderType orderType, Long channelId, Long refundId, RefundEnum.State sourceState, RefundEnum.State newState) {
        MQUtil.send(MQ.Tag.REFUND_STATE_EVENT, new RefundStateEvent(refundId, channelId, orderType, sourceState, newState));
    }

    @Override
    public void publishOrderDelivery(OrderEnum.OrderType orderType, Long channelId, String outOrderNo, List<SkuCountDTO> skuDeliverList, String expressName, String expressNo) {
        MQUtil.send(MQ.Tag.ORDER_DELIVERY_EVENT, new OrderDeliveryEvent(outOrderNo, channelId, orderType, skuDeliverList, expressName, expressNo));
    }

    @Override
    public void sendRefundOperationRecord(RefundDTO refund, RefundEnum.State from, RefundEnum.State to, com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum refundOperateTypeEnum) {
        try {
            String operationContent = refundOperateTypeEnum.getDesc();
            // 1. 组装消息对象（封装所有重复的参数赋值逻辑）
            RefundOperationRecordRPC recordRPC = buildRefundOperationRecordRPC(refund, from, to, operationContent, refundOperateTypeEnum);

            // 2. 发送消息（统一异常处理，避免消息发送失败导致主流程异常）
            MQUtil.send(MQ.Tag.REFUND_OPERATION_RECORD_EVENT, recordRPC);
            log.info("售后操作记录消息发送成功，refundId: {}, operationContent: {}", refund.getId(), operationContent);
        } catch (Exception e) {
            log.error("售后操作记录消息发送失败，refundId: {}", refund.getId(), e);
            // 消息发送失败不抛异常，避免影响主业务流程（可根据业务需求调整）
        }
    }

    /**
     * 构建售后操作记录消息对象（核心封装）
     */
    private RefundOperationRecordRPC buildRefundOperationRecordRPC(RefundDTO refund, RefundEnum.State beforeState,
                                                                   RefundEnum.State afterState, String operationContent, com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum operationType) {
        RefundOperationRecordRPC recordRPC = new RefundOperationRecordRPC();
        // 基础订单/售后单信息
        recordRPC.setSpuOrderId(refund.getSpuOrderId());
        recordRPC.setRefundId(refund.getId());
        // 操作人信息（封装重复的SecurityUtils调用）
        fillOperatorInfo(recordRPC);
        // 状态信息
        recordRPC.setBeforeState(beforeState);
        recordRPC.setAfterState(afterState);
        recordRPC.setOperationType(operationType);
        // 操作内容
        recordRPC.setOperationContent(operationContent);
        recordRPC.setRefundAmount(refund.getRefundAmount());
        recordRPC.setReason(buildReason(refund));
        return recordRPC;
    }

    /**
     * 填充操作人公共信息（抽离重复逻辑）
     */
    private void fillOperatorInfo(RefundOperationRecordRPC recordRPC) {
        if (ObjectUtil.isNull(SecurityUtils.getAccountId())){
            recordRPC.setOperatorId(0L);
            recordRPC.setOperatorRoleCode(AccountEnum.Identity.PLATFORM);
            recordRPC.setOperatorClient(AccountEnum.Client.ADMIN.getCode());
            recordRPC.setOperatorName("系统");
        }else {
            recordRPC.setOperatorId(SecurityUtils.getAccountId());
            recordRPC.setOperatorRoleCode(SecurityUtils.getIdentity());
            recordRPC.setOperatorClient(SecurityUtils.getClient().name());
            recordRPC.setOperatorName(SecurityUtils.getUsername());
        }
    }

    /**
     * 构建操作原因（统一格式）
     */
    private String buildReason(RefundDTO refund) {
        return refund.getReason() + " : " + refund.getRemark();
    }

    /**
     * 支付成功通知
     * 1. 仅通知选品的sku订单
     */
    @Override
    public void paySuccessNotify(OrderAgg orderAgg) {
        // FIXME[goods-pay-event-removed]: 选品支付成功事件(GoodsPaySuccessEvent/goodsOrderPaySuccess)迁移期已删,
        // 待新履约模型接入后以新事件替换。方法暂留空壳(私有且当前无调用方)。
//        GoodsPaySuccessEvent goodsPaySuccessEvent = new GoodsPaySuccessEvent();
//        goodsPaySuccessEvent.setOrderId(orderAgg.getOrder().getId());
//        List<SkuOrderMessageVO> skuOrderMessageVOList = new ArrayList<>();
//        Map<Long, SpuOrderDTO> spuOrderMap = orderAgg.getSpuOrderList().stream().collect(Collectors.toMap(SpuOrderDTO::getSpuId, Function.identity()));
//        for (SkuOrderDTO skuOrder : orderAgg.getSkuOrderList()) {
//            SkuOrderMessageVO skuOrderMessageVO = TransferUtils.transfer(skuOrder,SkuOrderMessageVO.class);
//            skuOrderMessageVO.setChannelId(orderAgg.getOrder().getChannelId());
//            SpuOrderDTO spuOrder = spuOrderMap.get(skuOrder.getSpuId());
//            skuOrderMessageVO.setSpuChannelType(spuOrder.getSpuChannelType());
//            skuOrderMessageVOList.add(skuOrderMessageVO);
//        }
//        List<SpuOrderMessageVO> spuOrderMessageVOList = new ArrayList<>();
//        for (SpuOrderDTO spuOrder : orderAgg.getSpuOrderList()) {
//            SpuOrderMessageVO spuOrderMessageVO = TransferUtils.transfer(spuOrder,SpuOrderMessageVO.class);
//            spuOrderMessageVOList.add(spuOrderMessageVO);
//        }
//        goodsPaySuccessEvent.setSkuOrderList(skuOrderMessageVOList);
//        goodsPaySuccessEvent.setSpuOrderList(spuOrderMessageVOList);
//        //支付成功通知
//        orderRepository.goodsOrderPaySuccess(goodsPaySuccessEvent);
    }

    @Override
    public void orderExpireClose(Long orderId) {
        MQUtil.sendDelayed(MQ.Tag.TIME_OUT_CLOSE_ORDER_EVENT, orderId, MQEnum.DelayTimeLevel.MINUTE_30.getLevel());
    }
}
