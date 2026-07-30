package com.newzkl.platform.base.biz.order.application.service.impl;

import com.newzkl.platform.base.biz.order.application.service.UpdateOrderService;
import com.newzkl.platform.base.biz.order.model.order.req.CancelOrderReq;
import com.newzkl.platform.base.biz.order.model.order.req.ConfirmOrderReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderAddressUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.res.TripSpuOrderChangeRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单变更应用服务实现 (写编排)
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.service.impl.CommitOrderImpl} (改址) 与
 * {@code OrderServiceImpl} (取消/收货/完成/状态同步)。旧实现的写编排全部建立在订单主聚合
 * {@code OrderAgg} + {@code IOrderDomain} 之上 (聚合内做状态机校验、库存回滚、退款、
 * 本地消息投递), 中台 biz-order 目前只有状态记录/发货/结算三个领域服务, 无订单主聚合,
 * 因此本类当前全部方法为 domain-gap。</p>
 *
 * <p>本类存在的意义: 让注入 {@code UpdateOrderService} 的 controller 能完成 bean 装配,
 * 使应用可启动; 调用具体端点时立刻抛 {@code UnsupportedOperationException} 暴露缺口,
 * 而不是用半截逻辑改坏订单状态或资金流。</p>
 *
 * <p><b>domain-gap 清单 (未实现方法 → 缺失能力):</b></p>
 * <ul>
 *   <li>{@link UpdateOrderServiceImpl#changeOrderShip} — 缺订单收货信息变更领域能力: 旧
 *       {@code IOrderDomain#changeOrderShip} 需校验订单可改址状态、校验改址后运费不变、
 *       回写 SPU 订单 {@code receiptInfo} 快照并同步预支付单; 中台 {@code OrderRepository}
 *       无收货信息更新方法, 亦无运费重算端口。</li>
 *   <li>{@link UpdateOrderServiceImpl#cancelOrder} — 缺取消订单编排: 旧流程 = 聚合状态机校验 + 库存释放
 *       ({@code InventoryExecuteCommand}) + 已付款退款 + 状态链更新 + 操作记录事件;
 *       中台无 {@code OrderAgg}, 状态机与退款联动无落点。</li>
 *   <li>{@link UpdateOrderServiceImpl#confirmOrder} — 缺确认收货编排: 旧 {@code receiveSkuOrder} 在聚合内做
 *       SKU 逐条收货 + 供应商结算配置判定 + 待结算单生成 ({@code SettleDomain}) + 状态同步;
 *       中台缺聚合与 SKU 批量状态流转能力。</li>
 *   <li>{@link UpdateOrderServiceImpl#completeOrder} — 缺完成订单编排: 旧 {@code completeSkuOrder} 同上,
 *       且需按结算类型 {@code settleType} 生成待结算单。</li>
 *   <li>{@link UpdateOrderServiceImpl#tripSpuOrderChange} — 缺订单状态同步领域能力: 旧
 *       {@code IOrderDomain#tripSpuOrderChange} 依据 SKU 订单状态反推 SPU/主订单目标状态并批量流转;
 *       中台仓储仅有单订单号状态链更新, 无批量状态推导能力。</li>
 * </ul>
 *
 * @author KC
 * @since 2026-07-28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateOrderServiceImpl implements UpdateOrderService {

    /**
     * 修改订单收货地址 (未实现)
     *
     * @param req 地址变更请求
     * @return 是否成功
     * @throws UnsupportedOperationException 中台缺订单收货信息变更领域能力与运费重算端口
     */
    @Override
    public Boolean changeOrderShip(OrderAddressUpdateReq req) {
        throw new UnsupportedOperationException(
                "TODO[domain-gap]: 缺订单收货信息变更领域能力 (旧 IOrderDomain#changeOrderShip: 可改址状态校验 + 运费不变校验 + receiptInfo 回写) 与运费重算端口");
    }

    /**
     * 取消订单 (未实现)
     *
     * @param req 取消订单请求
     * @return 是否成功
     * @throws UnsupportedOperationException 中台缺取消订单编排 (聚合状态机 + 库存释放 + 退款联动)
     */
    @Override
    public Boolean cancelOrder(CancelOrderReq req) {
        throw new UnsupportedOperationException(
                "TODO[domain-gap]: 缺取消订单编排 (旧 OrderAgg 状态机校验 + 库存释放 + 已付款退款 + 操作记录事件)");
    }

    /**
     * 确认收货 (未实现)
     *
     * @param req 确认收货请求
     * @return 是否成功
     * @throws UnsupportedOperationException 中台缺确认收货编排 (SKU 批量收货 + 待结算单生成)
     */
    @Override
    public Boolean confirmOrder(ConfirmOrderReq req) {
        throw new UnsupportedOperationException(
                "TODO[domain-gap]: 缺确认收货编排 (旧 IOrderDomain#receiveSkuOrder: SKU 批量收货 + 供应商结算配置判定 + 待结算单生成 + 状态同步)");
    }

    /**
     * 完成订单 (未实现)
     *
     * @param req 完成订单请求
     * @return 是否成功
     * @throws UnsupportedOperationException 中台缺完成订单编排 (SKU 批量完成 + 按结算类型生成待结算单)
     */
    @Override
    public Boolean completeOrder(ConfirmOrderReq req) {
        throw new UnsupportedOperationException(
                "TODO[domain-gap]: 缺完成订单编排 (旧 IOrderDomain#completeSkuOrder: SKU 批量完成 + 按 settleType 生成待结算单)");
    }

    /**
     * 订单状态同步 (未实现)
     *
     * @param orderNos    交易单号列表
     * @param spuOrderNos SPU 交易单号列表
     * @param skuOrderNos SKU 交易单号列表
     * @return 状态变更明细
     * @throws UnsupportedOperationException 中台缺订单状态批量推导与流转能力
     */
    @Override
    public TripSpuOrderChangeRes tripSpuOrderChange(List<String> orderNos, List<String> spuOrderNos,
                                                    List<String> skuOrderNos) {
        throw new UnsupportedOperationException(
                "TODO[domain-gap]: 缺订单状态同步领域能力 (旧 IOrderDomain#tripSpuOrderChange: 由 SKU 状态反推 SPU/主订单目标状态并批量流转)");
    }
}
