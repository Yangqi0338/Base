package com.newzkl.platform.base.biz.order.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderStateRecordRepository;
import com.newzkl.platform.base.biz.order.domain.service.OrderStateRecordDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderStateRecord;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordPageReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单状态记录 领域服务实现
 *
 * @author sijiwang
 * @since 2026-01-30
 */
@Service
@RequiredArgsConstructor
public class OrderStateRecordDomainImpl implements OrderStateRecordDomain {

    private final OrderStateRecordRepository orderStateRecordRepository;

    @Override
    public OrderStateRecord create(OrderStateRecord entity) {
        // 领域规则校验 - 新增必备字段非空校验
        Assert.notNull(entity, "订单状态记录不能为空");
        Assert.notNull(entity.getBeforeOrderState(), "变更前订单状态不能为空");
        Assert.notNull(entity.getAfterOrderState(), "变更后订单状态不能为空");
        Assert.notNull(entity.getOperateTime(), "操作时间不能为空");
        Assert.hasText(entity.getBeforeStateDesc(), "变更前订单状态描述不能为空");
        Assert.hasText(entity.getAfterStateDesc(), "变更后订单状态描述不能为空");
        Assert.isNull(entity.getId(), "新增时ID必须为空");

        // 初始化默认时间（未手动设置时自动填充）
        LocalDateTime now = LocalDateTime.now();
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(now);
        }
        if (entity.getUpdateTime() == null) {
            entity.setUpdateTime(now);
        }

        // 调用仓储层保存
        return orderStateRecordRepository.save(entity);
    }

    @Override
    public OrderStateRecord update(OrderStateRecord entity) {
        // 领域规则校验 - 修改必备字段非空校验
        Assert.notNull(entity, "订单状态记录不能为空");
        Assert.notNull(entity.getId(), "修改时ID不能为空");
        
        // 校验记录是否存在（仓储无existsById时，通过订单ID关联校验）
        List<OrderStateRecord> existRecords = orderStateRecordRepository.findBySpuOrderNo(entity.getSpuOrderNo());
        Assert.isTrue(!existRecords.isEmpty() && existRecords.stream().anyMatch(r -> r.getId().equals(entity.getId())),
                "订单状态记录不存在，ID：" + entity.getId());

        // 核心字段非空校验（仅校验传递的非空字段）
        if (entity.getBeforeOrderState() != null) {
            Assert.notNull(entity.getBeforeOrderState(), "变更前订单状态不能为空");
        }
        if (entity.getAfterOrderState() != null) {
            Assert.notNull(entity.getAfterOrderState(), "变更后订单状态不能为空");
        }
        if (entity.getOperateTime() != null) {
            Assert.notNull(entity.getOperateTime(), "操作时间不能为空");
        }
        if (entity.getBeforeStateDesc() != null) {
            Assert.hasText(entity.getBeforeStateDesc(), "变更前订单状态描述不能为空");
        }
        if (entity.getAfterStateDesc() != null) {
            Assert.hasText(entity.getAfterStateDesc(), "变更后订单状态描述不能为空");
        }

        // 更新时间戳
        entity.setUpdateTime(LocalDateTime.now());

        // 调用仓储层保存（更新）
        return orderStateRecordRepository.save(entity);
    }

    @Override
    public List<OrderStateRecord> listByOrderNo(String spuOrderNo) {
        // 参数校验
        Assert.notNull(spuOrderNo, "订单ID不能为空");
        
        // 调用仓储层查询
        return orderStateRecordRepository.findBySpuOrderNo(spuOrderNo);
    }

    @Override
    public Optional<OrderStateRecord> findByOrderIdAndAfterOrderState(String spuOrderNo, Integer afterOrderState) {
        // 参数校验
        Assert.notNull(spuOrderNo, "订单ID不能为空");
        Assert.notNull(afterOrderState, "变更后订单状态不能为空");
        
        // 仓储方法的orderState参数对应拆分后的afterOrderState
        return orderStateRecordRepository.findBySpuOrderNoAndOrderState(spuOrderNo, afterOrderState);
    }

    @Override
    public List<OrderStateRecord> findByOperateTimeBetween(LocalDateTime startTime, LocalDateTime endTime, int limit) {
        // 参数校验
        Assert.notNull(startTime, "开始时间不能为空");
        Assert.notNull(endTime, "结束时间不能为空");
        Assert.isTrue(startTime.isBefore(endTime), "开始时间必须早于结束时间");
        Assert.isTrue(limit > 0, "查询数量限制必须大于0");
        
        // 调用仓储层查询
        return orderStateRecordRepository.findByOperateTimeBetween(startTime, endTime, limit);
    }

    @Override
    public Page<OrderStateRecord> findPage(OrderStateRecordPageReq req) {
        return orderStateRecordRepository.findPage(req);
    }

    @Override
    public List<OrderStateRecord> listBySpuOrderId(String spuOrderNo) {
        return  orderStateRecordRepository.listBySpuOrderId(spuOrderNo);
    }
}