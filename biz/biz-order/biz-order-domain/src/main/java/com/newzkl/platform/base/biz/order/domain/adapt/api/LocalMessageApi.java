package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.dto.RefundDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuCountDTO;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSyncHandleVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;

import java.util.List;

/**
 * 本地消息出站端口
 *
 * <p>迁移: 原 {@code @DubboReference ILocalMessageFacade}(new-scm scm-message-rpc) 幻想跨域接口,
 * Base 单体现态下抽为本地端口, 由 infra 实现直接经 {@code MQUtil} 投递(本地消息表 + MQ)。
 * 将来拆服务时改 impl 为远程 consumer, 领域/应用层零改动。</p>
 *
 * @author KC
 */
public interface LocalMessageApi {

    /**
     * 发送消息 (默认主题 {@code scm_main})
     *
     * @param tag            消息标签
     * @param messageContent 消息内容对象
     * @param messageClass   消息内容类全限定名
     */
    void sendMessage(String tag, Object messageContent, String messageClass);

    void sendRefundPassMessage(RefundDTO refund);

    /**
     * 唤醒分润延迟消息
     * @param skuOrderId sku订单维度
     */
    void wakeUpEarningMessage(Long skuOrderId);

    void sendOrderNewRecordEvent(List<OrderDTO> orderList, OrderEnum.State beforeOrderState, OrderEnum.State afterOrderState, Long operatorId, AccountEnum.Identity operatorRoleId);

    void sendRefundOperationRecord(RefundDTO refundVO, RefundEnum.State from, RefundEnum.State to, com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum refundOperateTypeEnum);

    void paySuccessNotify(OrderAgg orderAgg);

    /**
     * 订单过期关闭
     */
    void orderExpireClose(Long orderId);

    /**
     * 发布订单状态变更业务事件
     *
     * <p>无条件发布 不做订单类型过滤: 由订阅方自行判定是否关心。原
     * {@code OrderRepository.orderStateNotify} 直接发 openapi 专用通知信封, 已废弃</p>
     *
     * @param orderType    订单类型
     * @param channelId    下单主体账户ID
     * @param outOrderNo   外部订单号
     * @param sourceState  变更前状态
     * @param newState     变更后状态
     */
    void publishOrderState(OrderEnum.OrderType orderType, Long channelId, String outOrderNo, OrderEnum.State sourceState, OrderEnum.State newState);

    /**
     * 发布售后单状态变更业务事件
     *
     * <p>无条件发布 不做订单类型过滤: 由订阅方自行判定是否关心</p>
     *
     * @param orderType   订单类型
     * @param channelId   下单主体账户ID
     * @param refundId    售后单ID
     * @param sourceState 变更前状态
     * @param newState    变更后状态
     */
    void publishRefundState(OrderEnum.OrderType orderType, Long channelId, Long refundId, RefundEnum.State sourceState, RefundEnum.State newState);

    /**
     * 发布订单发货业务事件
     *
     * <p>无条件发布 不做订单类型过滤: 由订阅方自行判定是否关心。原 {@code deliverNotify}
     * 直接发 openapi 专用通知信封, 已废弃</p>
     *
     * @param orderType      订单类型
     * @param channelId      下单主体账户ID
     * @param outOrderNo     外部订单号
     * @param skuDeliverList 本次发货的SKU与数量
     * @param expressName    快递公司名称
     * @param expressNo      快递单号
     */
    void publishOrderDelivery(OrderEnum.OrderType orderType, Long channelId, String outOrderNo, List<SkuCountDTO> skuDeliverList, String expressName, String expressNo);
}
