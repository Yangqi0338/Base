package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderStateRecordRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.OrderStateRecordDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.OrderStateRecordDO;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderStateRecord;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordPageReq;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 订单状态记录仓储实现类
 * @author sijiwang
 */
@Repository
public class OrderStateRecordRepositoryImpl extends ServiceImpl<OrderStateRecordDAO, OrderStateRecordDO> implements OrderStateRecordRepository {

    @Override
    public OrderStateRecord save(OrderStateRecord record) {
        OrderStateRecordDO orderStateRecordDO = TransferUtils.transfer(record,OrderStateRecordDO::new);
        if (record.getId() == null){
            baseMapper.insert(orderStateRecordDO);
            record.setId(orderStateRecordDO.getId());
        }else {
            baseMapper.updateById(orderStateRecordDO);
        }
        return record;
    }

    @Override
    public List<OrderStateRecord> findBySpuOrderNo(String spuOrderNo) {
        LambdaQueryWrapper<OrderStateRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderStateRecordDO::getSpuOrderNo, spuOrderNo)
                .orderByDesc(OrderStateRecordDO::getOperateTime);
        List<OrderStateRecordDO> orderStateRecordDOS = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(orderStateRecordDOS)){
            return Collections.emptyList();
        }
        return TransferUtils.transfers(orderStateRecordDOS, OrderStateRecord::new);
    }

    @Override
    public Optional<OrderStateRecord> findBySpuOrderNoAndOrderState(String spuOrderNo, Integer orderState) {
        LambdaQueryWrapper<OrderStateRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderStateRecordDO::getSpuOrderNo, spuOrderNo)
                .eq(OrderStateRecordDO::getBeforeOrderState, orderState)
                .last("LIMIT 1");
        return Optional.ofNullable(baseMapper.selectOne(wrapper))
                .map(doObj -> TransferUtils.transfer(doObj, OrderStateRecord::new));
    }

    @Override
    public List<OrderStateRecord> findByOperateTimeBetween(LocalDateTime startTime, LocalDateTime endTime,
                                                           int limit) {
        LambdaQueryWrapper<OrderStateRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(OrderStateRecordDO::getOperateTime, startTime, endTime)
                .orderByDesc(OrderStateRecordDO::getOperateTime)
                .last("LIMIT " + limit);
        List<OrderStateRecordDO> orderStateRecordDOS = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(orderStateRecordDOS)){
            return Collections.emptyList();
        }
        return TransferUtils.transfers(orderStateRecordDOS, OrderStateRecord::new);
    }

    @Override
    public Page<OrderStateRecord> findPage(OrderStateRecordPageReq req) {
        // 1. 构建DO层分页对象，复用传入的分页参数（当前页、页大小）
        Page<OrderStateRecordDO> doPage = new Page<>(req.getCurrent(), req.getSize());

        // 2. 构建查询条件
        LambdaQueryWrapper<OrderStateRecordDO> wrapper = new LambdaQueryWrapper<>();

        if (req.getOrderNo() != null) {
            wrapper.eq(OrderStateRecordDO::getOrderNo, req.getOrderNo());
        }
        if (req.getSpuOrderNo() != null) {
            wrapper.eq(OrderStateRecordDO::getSpuOrderNo, req.getSpuOrderNo());
        }
        if (req.getAfterOrderState() != null) {
            wrapper.eq(OrderStateRecordDO::getAfterOrderState, req.getAfterOrderState());
        }
        if (req.getOperatorRoleId() != null) {
            wrapper.eq(OrderStateRecordDO::getOperatorRoleId, req.getOperatorRoleId());
        }
        // 按创建时间降序排序（核心要求）
        wrapper.orderByDesc(OrderStateRecordDO::getCreateTime);

        // 3. 执行MyBatisPlus分页查询（自动计算总条数、分页数据）
        Page<OrderStateRecordDO> resultDoPage = baseMapper.selectPage(doPage, wrapper);

        // 4. 转换DO分页结果为领域实体分页结果（MyBatisPlus的convert方法一键转换）
        return TransferUtils.transferPage(resultDoPage, OrderStateRecord::new);
    }

    @Override
    public List<OrderStateRecord> listBySpuOrderId(String spuOrderNo) {
        LambdaQueryWrapper<OrderStateRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderStateRecordDO::getSpuOrderNo, spuOrderNo)
                .orderByDesc(OrderStateRecordDO::getOperateTime);
        List<OrderStateRecordDO> orderStateRecordDOS = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(orderStateRecordDOS)){
            return Collections.emptyList();
        }
        return TransferUtils.transfers(orderStateRecordDOS, OrderStateRecord::new);
    }
}
