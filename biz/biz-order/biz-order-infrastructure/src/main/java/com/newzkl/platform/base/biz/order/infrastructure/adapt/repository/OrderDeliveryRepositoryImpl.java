package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderDeliveryRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.OrderDeliveryDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.OrderDeliveryDO;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderDelivery;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryPageReq;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 订单发货主单仓储实现
 * @author sijiwang
 */
@Repository
public class OrderDeliveryRepositoryImpl extends ServiceImpl<OrderDeliveryDAO, OrderDeliveryDO> implements OrderDeliveryRepository {

    /**
     * 保存/更新发货单
     */
    @Override
    public OrderDelivery save(OrderDelivery delivery) {
        OrderDeliveryDO deliveryDO = TransferUtils.transfer(delivery, OrderDeliveryDO::new);
        this.saveOrUpdate(deliveryDO);
        return TransferUtils.transfer(deliveryDO, OrderDelivery::new);
    }

    /**
     * 根据ID查询
     */
    @Override
    public Optional<OrderDelivery> findById(Long id) {
        OrderDeliveryDO deliveryDO = this.getById(id);
        return Optional.ofNullable(TransferUtils.transfer(deliveryDO, OrderDelivery::new));
    }

    /**
     * 根据发货单号查询
     */
    @Override
    public Optional<OrderDelivery> findByDeliveryNo(String deliveryNo) {
        if (StringUtils.isBlank(deliveryNo)) {
            return Optional.empty();
        }
        LambdaQueryWrapper<OrderDeliveryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDeliveryDO::getDeliveryNo, deliveryNo);
        OrderDeliveryDO deliveryDO = this.getOne(wrapper);
        return Optional.ofNullable(TransferUtils.transfer(deliveryDO, OrderDelivery::new));
    }

    /**
     * 根据订单号查询发货记录
     */
    @Override
    public List<OrderDelivery> findByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderDeliveryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(orderNo), OrderDeliveryDO::getOrderNo, orderNo);
        List<OrderDeliveryDO> doList = this.list(wrapper);
        return TransferUtils.transfers(doList, OrderDelivery::new);
    }

    /**
     * 分页多条件查询
     * 支持：deliveryNo、orderNo、spuOrderNo、storeId、userId
     */
    @Override
    public Page<OrderDelivery> pageQuery(OrderDeliveryPageReq pageReq) {
        LambdaQueryWrapper<OrderDeliveryDO> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.isNotBlank(pageReq.getDeliveryNo()), OrderDeliveryDO::getDeliveryNo, pageReq.getDeliveryNo());
        wrapper.eq(StringUtils.isNotBlank(pageReq.getOrderNo()), OrderDeliveryDO::getOrderNo, pageReq.getOrderNo());
        wrapper.eq(StringUtils.isNotBlank(pageReq.getSpuOrderNo()), OrderDeliveryDO::getSpuOrderNo, pageReq.getSpuOrderNo());
        wrapper.eq(pageReq.getStoreId() != null, OrderDeliveryDO::getStoreId, pageReq.getStoreId());
        wrapper.eq(pageReq.getUserId() != null, OrderDeliveryDO::getUserId, pageReq.getUserId());
        wrapper.eq(pageReq.getDeliveryStatus() != null, OrderDeliveryDO::getDeliveryStatus, pageReq.getDeliveryStatus());

        wrapper.orderByDesc(OrderDeliveryDO::getId);

        Page<OrderDeliveryDO> page = new Page<>(pageReq.getCurrent(), pageReq.getSize());
        Page<OrderDeliveryDO> doPage = this.page(page, wrapper);

        // 使用 TransferUtils 正确的分页转换方法
        Page<OrderDelivery> resultPage = new Page<>();
        resultPage.setCurrent(doPage.getCurrent());
        resultPage.setSize(doPage.getSize());
        resultPage.setTotal(doPage.getTotal());
        resultPage.setPages(doPage.getPages());
        resultPage.setRecords(TransferUtils.transfers(doPage.getRecords(), OrderDelivery::new));

        return resultPage;
    }
}