package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderStateRecordRepository;
import com.newzkl.platform.base.biz.order.infrastructure.assembler.OrderStateRecordAssembler;
import com.newzkl.platform.base.biz.order.infrastructure.dao.OrderStateRecordDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.OrderStateRecordDO;
import com.newzkl.platform.base.biz.order.model.dto.OrderStateRecordEntity;
import com.newzkl.platform.base.biz.order.model.req.OrderStateRecordPageReq;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单状态记录仓储实现类
 * @author sijiwang
 */
@Repository
public class OrderStateRecordRepositoryImpl extends ServiceImpl<OrderStateRecordDAO, OrderStateRecordDO> implements IOrderStateRecordRepository {
    @Resource
    private OrderStateRecordAssembler orderStateRecordAssembler;
    @Override
    public OrderStateRecordEntity save(OrderStateRecordEntity record) {
        OrderStateRecordDO orderStateRecordDO = orderStateRecordAssembler.domainToDO(record);
        if (record.getId() == null){
            baseMapper.insert(orderStateRecordDO);
            record.setId(orderStateRecordDO.getId());
        }else {
            baseMapper.updateById(orderStateRecordDO);
        }
        return record;
    }
}
