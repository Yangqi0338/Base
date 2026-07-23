package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OutOrderRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.OutOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.OutOrderDO;
import com.newzkl.platform.base.biz.order.model.order.dto.OutOrder;
import com.newzkl.platform.base.biz.order.model.order.req.OutOrderPageReq;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 外部订单仓储实现类
 *
 * @author sijiwang
 */
@Repository
public class OutOrderRepositoryImpl extends ServiceImpl<OutOrderDAO, OutOrderDO> implements OutOrderRepository {

    @Override
    public OutOrder save(OutOrder outOrder) {
        OutOrderDO outOrderDO = TransferUtils.transfer(outOrder, OutOrderDO::new);
        if (outOrder.getId() == null) {
            baseMapper.insert(outOrderDO);
            outOrder.setId(outOrderDO.getId());
        } else {
            baseMapper.updateById(outOrderDO);
        }
        return outOrder;
    }

    @Override
    public boolean batchSave(List<OutOrder> outOrders) {
        if (CollectionUtils.isEmpty(outOrders)) {
            return false;
        }
        List<OutOrderDO> outOrderDOs = TransferUtils.transfers(outOrders, OutOrderDO::new);
        return this.saveBatch(outOrderDOs);
    }

    @Override
    public Optional<OutOrder> findById(Long id) {
        return Optional.ofNullable(baseMapper.selectById(id))
                .map(doObj -> TransferUtils.transfer(doObj, OutOrder::new));
    }

    @Override
    public Optional<OutOrder> findByOrderSn(String orderSn) {
        LambdaQueryWrapper<OutOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutOrderDO::getOrderSn, orderSn);
        return Optional.ofNullable(baseMapper.selectOne(wrapper))
                .map(doObj -> TransferUtils.transfer(doObj, OutOrder::new));
    }

    @Override
    public List<OutOrder> findByOrderId(Long orderId) {
        LambdaQueryWrapper<OutOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutOrderDO::getOrderId, orderId);
        List<OutOrderDO> outOrderDOS = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(outOrderDOS)) {
            return Collections.emptyList();
        }
        return TransferUtils.transfers(outOrderDOS, OutOrder::new);
    }

    @Override
    public boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public Page<OutOrder> findPage(OutOrderPageReq req) {
        // 构建DO层分页对象
        Page<OutOrderDO> doPage = new Page<>(req.getCurrent(), req.getSize());

        // 构建查询条件
        LambdaQueryWrapper<OutOrderDO> wrapper = new LambdaQueryWrapper<>();

        if (req.getOrderSn() != null) {
            wrapper.eq(OutOrderDO::getOrderSn, req.getOrderSn());
        }
        if (req.getOrderId() != null) {
            wrapper.eq(OutOrderDO::getOrderId, req.getOrderId());
        }

        // 按创建时间降序排序
        wrapper.orderByDesc(OutOrderDO::getId);

        // 执行MyBatisPlus分页查询
        Page<OutOrderDO> resultDoPage = baseMapper.selectPage(doPage, wrapper);

        // 转换DO分页结果为DTO分页结果
        return TransferUtils.transferPage(resultDoPage, OutOrder::new);
    }

    @Override
    public List<OutOrder> findByOrderSns(List<String> orderSns) {
        if (CollectionUtils.isEmpty(orderSns)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<OutOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OutOrderDO::getOrderSn, orderSns);
        List<OutOrderDO> outOrderDOS = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(outOrderDOS)) {
            return Collections.emptyList();
        }
        return TransferUtils.transfers(outOrderDOS, OutOrder::new);
    }
}