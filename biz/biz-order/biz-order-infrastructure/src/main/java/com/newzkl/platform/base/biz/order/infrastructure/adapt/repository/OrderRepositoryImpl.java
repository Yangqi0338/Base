package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.OrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SkuOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SpuOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.OrderDO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SkuOrderDO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SpuOrderDO;
import com.newzkl.platform.base.biz.order.model.order.dto.Order;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.res.CreateOrderRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 订单仓储实现
 *
 * @author sijiwang
 */
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl extends ServiceImpl<OrderDAO, OrderDO> implements OrderRepository {

    private final SpuOrderDAO spuOrderDAO;
    private final SkuOrderDAO skuOrderDAO;


    @Override
    public boolean save(Order order) {
        return Optional.ofNullable(order)
                // 直接调用TransferUtils转换，无需私有方法
                .map(source -> TransferUtils.transfer(source, OrderDO::new))
                .map(doObj -> {
                    doObj.preInsert();
                    return saveOrUpdate(doObj);
                }).orElse(false);
    }

    @Override
    public boolean updateById(Order order) {
        return Optional.ofNullable(order)
                .map(source -> TransferUtils.transfer(source, OrderDO::new))
                .map(doObj -> {
                    doObj.preUpdate();
                    return updateById(doObj);
                }).orElse(false);
    }

    @Override
    public Order getById(Long id) {
        return Optional.ofNullable(id)
                .map(baseMapper::selectById)
                // 直接转换DO到Model
                .map(doObj -> TransferUtils.transfer(doObj, Order::new))
                .orElse(null);
    }

    @Override
    public Order getByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<OrderDO>().eq(OrderDO::getOrderNo, orderNo);
        return Optional.ofNullable(baseMapper.selectOne(wrapper))
                .map(doObj -> TransferUtils.transfer(doObj, Order::new))
                .orElse(null);
    }

    @Override
    public Page<Order> pageQuery(Page<Order> page, Order queryCondition) {
        Page<OrderDO> doPage = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<OrderDO> wrapper = buildQueryWrapper(queryCondition);
        return TransferUtils.transferPage(baseMapper.selectPage(doPage, wrapper), Order::new);
    }

    @Override
    public boolean batchSave(List<Order> orderList) {
        if (CollectionUtil.isEmpty(orderList)) {
            return false;
        }
        // 直接转换集合，无需私有方法
        List<OrderDO> doList = TransferUtils.transfers(orderList, OrderDO::new);
        doList.forEach(OrderDO::preInsert);
        return saveBatch(doList);
    }

    @Override
    @Transactional
    public void orderAggSave(CreateOrderRes createOrderRes) {
        // 1. 保存主订单 (Order)
        Order order = createOrderRes.getOrder();
        OrderDO orderDO = TransferUtils.transfer(order, OrderDO::new);
        orderDO.preInsert();
        baseMapper.insert(orderDO); // 保存主订单，自动生成id
        Long orderId = orderDO.getId(); // 获取主订单ID

        // 2. 处理订单商品明细 (OrderItem)
        List<CreateOrderRes.OrderItem> orderItems = createOrderRes.getOrderItems();
        if (CollectionUtil.isEmpty(orderItems)) {
            return;
        }

        for (CreateOrderRes.OrderItem orderItem : orderItems) {
            // 2.1 保存SPU层级订单 (SpuOrder)
            SpuOrder spuOrder = orderItem.getSpuOrder();
            SpuOrderDO spuOrderDO = TransferUtils.transfer(spuOrder, SpuOrderDO::new);
            spuOrderDO.preInsert();
            spuOrderDAO.insert(spuOrderDO); // 保存SPU订单
            Long spuOrderId = spuOrderDO.getId(); // 获取SPU订单ID

            // 2.2 保存SKU层级订单 (SkuOrder)
            List<SkuOrder> skuOrders = orderItem.getSkuOrders();
            if (CollectionUtil.isEmpty(skuOrders)) {
                continue;
            }

            List<SkuOrderDO> skuOrderDOs = TransferUtils.transfers(skuOrders, SkuOrderDO::new);
            for (SkuOrderDO skuOrderDO : skuOrderDOs) {
                skuOrderDO.preInsert();
            }
            skuOrderDAO.insert(skuOrderDOs); // 批量保存SKU订单
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderChainStateByOrderNo(String orderNo, Integer sourceState, Integer toState, String closeReason) {
        // 1. 参数校验
        if (orderNo == null || StrUtil.isBlank(orderNo)) {
            throw new IllegalArgumentException("订单号不能为空");
        }
        if (toState == null) {
            throw new IllegalArgumentException("目标状态不能为空");
        }

        // 2. 更新主订单状态
        int orderAffected = updateMainOrderStateByOrderNo(orderNo, sourceState, toState, closeReason);

        // 3. 更新SPU订单状态（若主订单更新成功）
        if (orderAffected > 0) {
            spuOrderDAO.updateSpuOrderStateByOrderNo(orderNo, sourceState, toState, closeReason);
            // 4. 更新SKU订单状态
            skuOrderDAO.updateSkuOrderStateByOrderNo(orderNo, sourceState, toState,closeReason);
        }
    }

    @Override
    public int updateMainOrderStateByOrderNo(String orderNo, Integer sourceState, Integer toState, String closeReason) {
        return baseMapper.updateOrderStateByOrderNo(orderNo, sourceState, toState,closeReason);
    }

    private LambdaQueryWrapper<OrderDO> buildQueryWrapper(Order queryCondition) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        if (queryCondition == null) {
            return wrapper.orderByDesc(OrderDO::getCreateTime);
        }

        // 直接转换查询条件
        OrderDO queryDO = TransferUtils.transfer(queryCondition, OrderDO::new);
        Optional.ofNullable(queryDO.getOrderState()).ifPresent(state -> wrapper.eq(OrderDO::getOrderState, state));
        Optional.ofNullable(queryDO.getChannelId())
                .ifPresent(channelId -> wrapper.eq(OrderDO::getChannelId, channelId));

        return wrapper.orderByDesc(OrderDO::getCreateTime);
    }
}