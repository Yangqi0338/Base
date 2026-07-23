package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SkuOrderRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SkuOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SkuOrderDO;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.res.OrderStateCheckRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SKU订单仓储实现
 * @author sijiwang
 */
@Repository
public class SkuOrderRepositoryImpl extends ServiceImpl<SkuOrderDAO, SkuOrderDO> implements SkuOrderRepository {

    @Override
    public boolean save(SkuOrder skuOrder) {
        Assert.notNull(skuOrder, "保存SKU订单失败：订单对象不能为空");
        SkuOrderDO doObj = TransferUtils.transfer(skuOrder, SkuOrderDO::new);
        doObj.preInsert();
        return saveOrUpdate(doObj);
    }

    @Override
    public boolean updateById(SkuOrder skuOrder) {
        Assert.notNull(skuOrder, "更新SKU订单失败：订单对象不能为空");
        Assert.notNull(skuOrder.getId(), "更新SKU订单失败：订单ID不能为空");
        SkuOrderDO doObj = TransferUtils.transfer(skuOrder, SkuOrderDO::new);
        doObj.preUpdate();
        return updateById(doObj);
    }

    @Override
    public SkuOrder getById(Long id) {
        return Optional.ofNullable(id)
                .map(baseMapper::selectById)
                .map(doObj -> TransferUtils.transfer(doObj, SkuOrder::new))
                .orElse(null);
    }

    @Override
    public List<SkuOrder> listBySpuOrderNo(List<String> spuOrderNos) {
        return Optional.ofNullable(spuOrderNos)
                .filter(list -> !list.isEmpty())
                .map(list -> {
                    LambdaQueryWrapper<SkuOrderDO> wrapper = new LambdaQueryWrapper<>();
                    wrapper.in(SkuOrderDO::getSpuOrderNo, list)
                            .orderByDesc(SkuOrderDO::getCreateTime);
                    return baseMapper.selectList(wrapper).stream()
                            .filter(Objects::nonNull)
                            .map(doObj -> TransferUtils.transfer(doObj, SkuOrder::new))
                            .collect(Collectors.toList());
                }).orElse(Collections.emptyList());
    }

    @Override
    public List<SkuOrder> getBySpuOrderNo(String spuOrderNo) {
        return Optional.ofNullable(spuOrderNo)
                .filter(Objects::nonNull)
                .filter(str -> !StrUtil.isBlank(str))
                .map(no -> {
                    LambdaQueryWrapper<SkuOrderDO> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SkuOrderDO::getSpuOrderNo, no)
                            .orderByDesc(SkuOrderDO::getCreateTime);
                    return baseMapper.selectList(wrapper).stream()
                            .filter(Objects::nonNull)
                            .map(doObj -> TransferUtils.transfer(doObj, SkuOrder::new))
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public List<SkuOrder> listByOrderNo(String orderNo) {
        return Optional.ofNullable(orderNo)
                .filter(Objects::nonNull)
                .filter(str -> !StrUtil.isBlank(str))
                .map(no -> {
                    LambdaQueryWrapper<SkuOrderDO> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SkuOrderDO::getOrderNo, no)
                            .orderByDesc(SkuOrderDO::getCreateTime);
                    return baseMapper.selectList(wrapper).stream()
                            .filter(Objects::nonNull)
                            .map(doObj -> TransferUtils.transfer(doObj, SkuOrder::new))
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public Page<SkuOrder> pageQuery(Page<SkuOrder> page, SkuOrder skuOrder) {
        Assert.notNull(page, "分页查询SKU订单失败：分页对象不能为空");
        Page<SkuOrderDO> doPage = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<SkuOrderDO> wrapper = new LambdaQueryWrapper<>();

        Optional.ofNullable(skuOrder)
                .map(source -> TransferUtils.transfer(source, SkuOrderDO::new))
                .ifPresent(queryDO -> {
                    if (queryDO.getOrderState() != null) {
                        wrapper.eq(SkuOrderDO::getOrderState, queryDO.getOrderState());
                    }
                    if (queryDO.getSkuId() != null) {
                        wrapper.eq(SkuOrderDO::getSkuId, queryDO.getSkuId());
                    }
                });
        wrapper.orderByDesc(SkuOrderDO::getCreateTime);

        return TransferUtils.transferPage(baseMapper.selectPage(doPage, wrapper), SkuOrder::new);
    }

    @Override
    public boolean batchSaveSku(List<SkuOrder> skuOrderList) {
        if (skuOrderList == null || skuOrderList.isEmpty()) {
            return false;
        }
        List<SkuOrderDO> transfers = TransferUtils.transfers(skuOrderList, SkuOrderDO::new);
        return this.saveBatch(transfers);
    }

    @Override
    public boolean batchUpdateSku(List<SkuOrder> skuOrderList) {
        if (skuOrderList == null || skuOrderList.isEmpty()) {
            return false;
        }
        List<SkuOrderDO> transfers = TransferUtils.transfers(skuOrderList, SkuOrderDO::new);
        return this.saveOrUpdateBatch(transfers);
    }

    @Override
    public int updateSkuOrderStateByOrderNo(String orderNo, Integer sourceState, Integer toState, String closeReason) {
        return baseMapper.updateSkuOrderStateByOrderNo(orderNo, sourceState, toState, closeReason);
    }

    @Override
    public List<String> orderIdBySpuSkuOrderId(List<String> spuOrderNos, List<String> skuOrderNos) {
        if (CollectionUtil.isEmpty(spuOrderNos) && CollectionUtil.isEmpty(skuOrderNos)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SkuOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SkuOrderDO::getSpuOrderNo, spuOrderNos)
                .or()
                .in(SkuOrderDO::getSkuOrderNo, skuOrderNos);
        List<SkuOrderDO> list = this.list(wrapper);
        if (CollectionUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        return list.stream().map(SkuOrderDO::getOrderNo).collect(Collectors.toList());
    }

    @Override
    public List<OrderStateCheckRes> checkSpuOrderState(List<String> orderNos) {
        if (ObjectUtil.isEmpty(orderNos)) {
            return new ArrayList<>();
        }
        return baseMapper.checkSpuOrderState(orderNos);
    }
}