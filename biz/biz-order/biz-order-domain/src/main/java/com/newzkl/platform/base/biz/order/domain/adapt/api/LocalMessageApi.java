package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.dto.RefundDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuCountDTO;
import com.newzkl.platform.base.biz.order.model.dto.SpuOrderDTO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSyncHandleVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.facade.ModelShopOutVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.RefundOperateTypeEnum;

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
    void sendModelShopMessage(ModelShopOutVO modelShopOutVO);

    void sendRefundPassMessage(RefundDTO refund);

    /**
     * 渠道订单支付后同步处理 (投递 {@code scm_order/orderSyncHandle})
     *
     * @param orderSyncHandleVO 订单同步处理消息体
     */
    void orderChannelNodeHandle(OrderSyncHandleVO orderSyncHandleVO);

    /**
     * 唤醒分润延迟消息
     * @param skuOrderId sku订单维度
     */
    void wakeUpEarningMessage(Long skuOrderId);

    void sendOrderNewRecordEvent(List<SpuOrderDTO> spuOrderList, OrderEnum.State beforeOrderState, OrderEnum.State afterOrderState, Long operatorId, RoleEnum.CompanyRole operatorRoleId);

    void deliverNotify(String outOrderNo, List<SkuCountDTO> skuCountDTOList, String expressCompanyName, String expressNo, Long channelId);

    void sendRefundOperationRecord(RefundDTO refundVO, RefundEnum.State from, RefundEnum.State to, RefundOperateTypeEnum refundOperateTypeEnum);

    /**
     * 门店用户支付消息
     */
    void storeAccountPay(Long storeId, Long accountId, Money memberAmount);

    void paySuccessNotify(OrderAgg orderAgg);

    /**
     * 订单过期关闭
     */
    void orderExpireClose(Long orderId);
}
