package com.newzkl.platform.base.biz.order.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.OrderDeliveryAppService;
import com.newzkl.platform.base.biz.order.domain.service.OrderDeliveryDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderDelivery;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryUpdateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单发货应用服务实现 (发货编排)
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.service.impl.OrderServiceImpl#deliverCreate}
 * 及发货查询端点。写方法委托本域 {@code OrderDeliveryDomain}, 应用层只做编排与事务边界,
 * 不触碰 DO/DAO。</p>
 *
 * <p><b>domain-gap 清单 (已实现但语义不完整的方法 → 缺失能力):</b></p>
 * <ul>
 *   <li>{@link OrderDeliveryAppServiceImpl#deliverOrder} — 发货单落库与 SKU 发货数量更新已由领域服务完成;
 *       旧实现在此之后还有三段联动, 中台均无落点:
 *       (1) 发货后订单状态流转 (WAIT_DELIVERY → WAIT_RECEIVE) 与状态同步
 *       {@code tripSpuOrderChange} — 缺订单主聚合 {@code OrderDomain};
 *       (2) 运费待结算单生成 {@code SettleDomain#freightSettleOrderWaitSave} 与结算成功通知
 *       — 缺发货单到 SPU 订单金额/结算状态的读取编排;
 *       (3) 开发者发货通知 {@code orderRepository.deliverNotify} — 缺 openapi 通知端口接线。
 *       调用方需知悉: 当前发货仅落发货单, 订单状态不会自动流转。</li>
 * </ul>
 *
 * @author KC
 * @since 2026-07-28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderDeliveryAppServiceImpl implements OrderDeliveryAppService {

    private final OrderDeliveryDomain orderDeliveryDomain;

    /**
     * 订单发货: 创建发货单并回写 SKU 发货数量
     *
     * <p>注意: 发货后的订单状态流转、运费结算、开发者通知尚未接入 (见类注释 domain-gap)。</p>
     *
     * @param req 发货请求
     * @return 发货单领域模型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDelivery deliverOrder(OrderDeliveryCreateReq req) {
        OrderDelivery delivery = orderDeliveryDomain.createDelivery(req);
        log.warn("发货单已创建但订单状态流转/运费结算/开发者通知未接入, deliveryId={}",
                delivery == null ? null : delivery.getId());
        return delivery;
    }

    /**
     * 修改发货物流信息
     *
     * @param req 发货信息变更请求
     * @return 变更后的发货单领域模型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDelivery updateDelivery(OrderDeliveryUpdateReq req) {
        return orderDeliveryDomain.updateDelivery(req);
    }

    /**
     * 查询发货详情 (含发货明细)
     *
     * @param deliveryId 发货单 ID
     * @return 发货单领域模型, 不存在返回 null
     */
    @Override
    public OrderDelivery getDeliveryDetail(Long deliveryId) {
        return orderDeliveryDomain.getDeliveryDetail(deliveryId);
    }

    /**
     * 分页查询发货单列表
     *
     * @param pageReq 分页查询条件
     * @return 发货单分页结果
     */
    @Override
    public Page<OrderDelivery> pageDelivery(OrderDeliveryPageReq pageReq) {
        return orderDeliveryDomain.pageDelivery(pageReq);
    }
}
