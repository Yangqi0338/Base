package com.newzkl.platform.base.biz.order.domain.service.impl;

import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderStateRecordRepository;
import com.newzkl.platform.base.biz.order.domain.service.IOrderStateRecordDomainService;
import com.newzkl.platform.base.biz.order.model.dto.OrderStateRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * 订单状态记录 领域服务实现
 *
 * @author sijiwang
 * @since 2026-01-30
 */
@Service
public class OrderStateRecordDomainServiceImpl implements IOrderStateRecordDomainService {

    @Autowired
    private IOrderStateRecordRepository orderStateRecordRepository;

    @Override
    public OrderStateRecordEntity create(OrderStateRecordEntity entity) {
        // 领域规则校验 - 新增必备字段非空校验
        Assert.notNull(entity, "订单状态记录不能为空");
        Assert.notNull(entity.getOrderId(), "订单ID不能为空");
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
}
