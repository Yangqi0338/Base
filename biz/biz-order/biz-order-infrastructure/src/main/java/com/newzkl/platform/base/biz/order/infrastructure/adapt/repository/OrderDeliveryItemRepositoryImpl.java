package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderDeliveryItemRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.OrderDeliveryItemDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.OrderDeliveryItemDO;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderDeliveryItem;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 订单发货明细仓储实现
 * @author sijiwang
 */
@Repository
public class OrderDeliveryItemRepositoryImpl extends ServiceImpl<OrderDeliveryItemDAO, OrderDeliveryItemDO> implements OrderDeliveryItemRepository {

    /**
     * 批量保存发货明细
     */
    @Override
    public void saveBatch(List<OrderDeliveryItem> items) {
        List<OrderDeliveryItemDO> doList = TransferUtils.transfers(items, OrderDeliveryItemDO::new);
        this.saveBatch(doList);
    }

    /**
     * 根据发货单号查询所有明细
     */
    @Override
    public List<OrderDeliveryItem> findByDeliveryNo(String deliveryNo) {
        if (StringUtils.isBlank(deliveryNo)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<OrderDeliveryItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDeliveryItemDO::getDeliveryNo, deliveryNo);
        List<OrderDeliveryItemDO> doList = this.list(wrapper);
        return TransferUtils.transfers(doList, OrderDeliveryItem::new);
    }

    @Override
    public List<OrderDeliveryItem> selectBySkuOrderNos(List<String> skuOrderNos) {
        if (CollectionUtils.isEmpty(skuOrderNos)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<OrderDeliveryItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OrderDeliveryItemDO::getSkuOrderNo, skuOrderNos);
        List<OrderDeliveryItemDO> doList = this.list(wrapper);
        return TransferUtils.transfers(doList, OrderDeliveryItem::new);
    }
}