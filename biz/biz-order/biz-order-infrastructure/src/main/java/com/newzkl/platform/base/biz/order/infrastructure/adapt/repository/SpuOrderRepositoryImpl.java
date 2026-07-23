package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SpuOrderRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SpuOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SpuOrderDO;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.SpuOrderPageReq;
import com.newzkl.platform.base.biz.order.model.order.res.OrderStateCheckRes;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateCountVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SPU订单仓储实现
 * @author sijiwang
 */
@Repository
public class SpuOrderRepositoryImpl extends ServiceImpl<SpuOrderDAO, SpuOrderDO> implements SpuOrderRepository {

    @Override
    public boolean save(SpuOrder spuOrder) {
        Assert.notNull(spuOrder, "保存SPU订单失败：订单对象不能为空");
        SpuOrderDO doObj = TransferUtils.transfer(spuOrder, SpuOrderDO::new);
        doObj.preInsert();
        return saveOrUpdate(doObj);
    }

    @Override
    public boolean updateById(SpuOrder spuOrder) {
        Assert.notNull(spuOrder, "更新SPU订单失败：订单对象不能为空");
        Assert.notNull(spuOrder.getId(), "更新SPU订单失败：订单ID不能为空");
        SpuOrderDO doObj = TransferUtils.transfer(spuOrder, SpuOrderDO::new);
        doObj.preUpdate();
        return updateById(doObj);
    }

    @Override
    public SpuOrder getById(Long id) {
        return Optional.ofNullable(id)
                .map(baseMapper::selectById)
                .map(doObj -> TransferUtils.transfer(doObj, SpuOrder::new))
                .orElse(null);
    }

    @Override
    public SpuOrder getBySpuOrderNo(String spuOrderNo) {
        SpuOrderDO spuOrderDO = baseMapper.selectOne(new LambdaQueryWrapper<SpuOrderDO>()
                .eq(SpuOrderDO::getSpuOrderNo, spuOrderNo));
        return spuOrderDO != null ? TransferUtils.transfer(spuOrderDO, SpuOrder::new) : null;
    }

    @Override
    public List<SpuOrder> listBySpuOrderNo(String spuOrderNo) {
        return Optional.ofNullable(spuOrderNo)
                .filter(Objects::nonNull)
                .filter(str -> !StrUtil.isBlank(str))
                .map(no -> {
                    LambdaQueryWrapper<SpuOrderDO> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SpuOrderDO::getSpuOrderNo, no)
                            .orderByDesc(SpuOrderDO::getCreateTime);
                    return baseMapper.selectList(wrapper).stream()
                            .filter(Objects::nonNull)
                            .map(doObj -> TransferUtils.transfer(doObj, SpuOrder::new))
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public List<SpuOrder> listByOrderNo(String orderNo) {
        return Optional.ofNullable(orderNo)
                .filter(Objects::nonNull)
                .filter(str -> !StrUtil.isBlank(str))
                .map(no -> {
                    LambdaQueryWrapper<SpuOrderDO> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SpuOrderDO::getOrderNo, no)
                            .orderByDesc(SpuOrderDO::getCreateTime);
                    return baseMapper.selectList(wrapper).stream()
                            .filter(Objects::nonNull)
                            .map(doObj -> TransferUtils.transfer(doObj, SpuOrder::new))
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public Page<SpuOrder> pageQuery(SpuOrderPageReq req) {
        Assert.notNull(req, "分页查询SPU订单失败：分页对象不能为空");

        // 创建分页对象
        Page<SpuOrderDO> doPage = new Page<>(req.getCurrent(), req.getSize());

        // 构建查询条件
        LambdaQueryWrapper<SpuOrderDO> wrapper = buildQueryWrapper(req);

        // 执行查询
        Page<SpuOrderDO> resultPage = baseMapper.selectPage(doPage, wrapper);

        // 转换返回结果
        return TransferUtils.transferPage(resultPage, SpuOrder::new);
    }

    /**
     * 构建查询条件包装器
     *
     * @param req 查询请求参数
     * @return LambdaQueryWrapper
     */
    private LambdaQueryWrapper<SpuOrderDO> buildQueryWrapper(SpuOrderPageReq req) {
        LambdaQueryWrapper<SpuOrderDO> wrapper = new LambdaQueryWrapper<>();

        // 精确匹配查询条件
        Optional.ofNullable(req.getSpuOrderNo())
                .filter(StringUtils::isNotBlank)
                .ifPresent(spuOrderNo -> wrapper.eq(SpuOrderDO::getSpuOrderNo, spuOrderNo));

        Optional.ofNullable(req.getOrderNo())
                .filter(StringUtils::isNotBlank)
                .ifPresent(orderNo -> wrapper.eq(SpuOrderDO::getOrderNo, orderNo));

        Optional.ofNullable(req.getOutOrderNo())
                .filter(StringUtils::isNotBlank)
                .ifPresent(outOrderNo -> wrapper.eq(SpuOrderDO::getOutOrderNo, outOrderNo));

        Optional.ofNullable(req.getSourceType())
                .ifPresent(sourceType -> wrapper.eq(SpuOrderDO::getSourceType, sourceType));

        Optional.ofNullable(req.getOrderState())
                .ifPresent(orderState -> wrapper.eq(SpuOrderDO::getOrderState, orderState));

        Optional.ofNullable(req.getOrderType())
                .ifPresent(orderType -> wrapper.eq(SpuOrderDO::getOrderType, orderType));

        Optional.ofNullable(req.getChannelId())
                .ifPresent(channelId -> wrapper.eq(SpuOrderDO::getChannelId, channelId));

        Optional.ofNullable(req.getSupplierId())
                .ifPresent(supplierId -> wrapper.eq(SpuOrderDO::getSupplierId, supplierId));

        Optional.ofNullable(req.getStoreId())
                .ifPresent(storeId -> wrapper.eq(SpuOrderDO::getStoreId, storeId));

        // ID集合查询
        Optional.ofNullable(req.getSpuOrderNoList())
                .filter(list -> !list.isEmpty())
                .ifPresent(spuOrderNoList -> wrapper.in(SpuOrderDO::getSpuOrderNo, spuOrderNoList));

        Optional.ofNullable(req.getMemberIdList())
                .filter(list -> !list.isEmpty())
                .ifPresent(memberIdList -> wrapper.in(SpuOrderDO::getUserId, memberIdList));

        Optional.ofNullable(req.getChannelIdList())
                .filter(list -> !list.isEmpty())
                .ifPresent(channelIdList -> wrapper.in(SpuOrderDO::getChannelId, channelIdList));

        Optional.ofNullable(req.getSupplierIdList())
                .filter(list -> !list.isEmpty())
                .ifPresent(supplierIdList -> wrapper.in(SpuOrderDO::getSupplierId, supplierIdList));

        Optional.ofNullable(req.getOrderStateList())
                .filter(list -> !list.isEmpty())
                .ifPresent(orderStateList -> wrapper.in(SpuOrderDO::getOrderState, orderStateList));

        // 时间范围查询
        Optional.ofNullable(req.getCreateBeginTime())
                .filter(StringUtils::isNotBlank)
                .ifPresent(createBeginTime -> wrapper.ge(SpuOrderDO::getCreateTime, createBeginTime));

        Optional.ofNullable(req.getCreateEndTime())
                .filter(StringUtils::isNotBlank)
                .ifPresent(createEndTime -> wrapper.le(SpuOrderDO::getCreateTime, createEndTime));

        Optional.ofNullable(req.getPayBeginTime())
                .filter(StringUtils::isNotBlank)
                .ifPresent(payBeginTime -> wrapper.ge(SpuOrderDO::getPayTime, payBeginTime));

        Optional.ofNullable(req.getPayEndTime())
                .filter(StringUtils::isNotBlank)
                .ifPresent(payEndTime -> wrapper.le(SpuOrderDO::getPayTime, payEndTime));

        // 金额范围查询
        Optional.ofNullable(req.getMemberAmountMin())
                .ifPresent(min -> wrapper.ge(SpuOrderDO::getUserPayAmount, min * 100L));

        Optional.ofNullable(req.getMemberAmountMax())
                .ifPresent(max -> wrapper.le(SpuOrderDO::getUserPayAmount, max * 100L));

        // 退款状态查询
        Optional.ofNullable(req.getRefund())
                .ifPresent(refund -> {
                    if (refund == 0) {
                        // 未退款：isRefunding = 0 或者为null
                        wrapper.and(wrapper1 -> wrapper1.eq(SpuOrderDO::getIsRefunding, 0).or().isNull(SpuOrderDO::getIsRefunding));
                    } else {
                        // 已退款：isRefunding = 1
                        wrapper.eq(SpuOrderDO::getIsRefunding, 1);
                    }
                });

        // 按创建时间降序排列
        wrapper.orderByDesc(SpuOrderDO::getCreateTime);

        return wrapper;
    }

    @Override
    public boolean batchSave(List<SpuOrder> spuOrderList) {
        if (spuOrderList == null || spuOrderList.isEmpty()) {
            return false;
        }
        List<SpuOrderDO> doList = spuOrderList.stream()
                .filter(Objects::nonNull)
                .map(source -> TransferUtils.transfer(source, SpuOrderDO::new))
                .peek(SpuOrderDO::preInsert)
                .collect(Collectors.toList());
        return saveBatch(doList);
    }

    @Override
    public boolean batchUpdateSpu(List<SpuOrder> spuOrderList) {
        if (spuOrderList == null || spuOrderList.isEmpty()) {
            return false;
        }
        List<SpuOrderDO> transfers = TransferUtils.transfers(spuOrderList, SpuOrderDO::new);
        return this.saveOrUpdateBatch(transfers);
    }

    @Override
    public int updateSpuOrderStateByOrderNo(String orderNo, Integer sourceState, Integer toState, String spuOrderExt) {
        return baseMapper.updateSpuOrderStateByOrderNo(orderNo, sourceState, toState, spuOrderExt);
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByChannel(Long channelId) {
        return baseMapper.countOrderStateByChannel(channelId);
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByAccount(Long accountId) {
        return baseMapper.countOrderStateByAccount(accountId);
    }

    @Override
    public List<OrderStateCheckRes> checkOrderState(List<String> orderNos) {
        return baseMapper.checkOrderState(orderNos);
    }
}