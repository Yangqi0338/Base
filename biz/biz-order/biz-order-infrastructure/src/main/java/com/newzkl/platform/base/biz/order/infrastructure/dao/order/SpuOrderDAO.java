package com.newzkl.platform.base.biz.order.infrastructure.dao.order;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SpuOrderDO;

import com.newzkl.platform.base.biz.order.model.req.query.SpuOrderQuery;
import com.newzkl.platform.base.biz.order.model.dto.OrderStateCheckDTO;
import com.newzkl.platform.base.biz.order.model.res.SpuRefundRes;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* SPU订单
* @author fang
*/
@Mapper
@Repository
public interface SpuOrderDAO extends BaseMapper<SpuOrderDO> {

    BizCountMap countMapWithOrderByQuery(@Param(Constants.WRAPPER) AbstractWrapper<?, ?, ?> wrapper, @Param("query") SpuOrderQuery query);

    /**
     * 统计订单状态数量
     * @return 订单状态统计列表
     */
    BizCountMap countMap(@Param(Constants.WRAPPER) AbstractWrapper<?, ?, ?> wrapper);

    List<OrderStateCheckDTO> checkSpuOrderState(@Param("orderId") List<Long> orderId);

    List<SpuRefundRes> spuRefundResList(@Param(Constants.WRAPPER) AbstractWrapper<?, ?, ?> wrapper);

    List<GroupCountRes> orderCount(@Param("query") TimeQuery timeQuery);

    int updateSpuRefundingCount(@Param("spuOrderId") Long spuOrderId, @Param("count") Integer count);

    int cutSkuOrderRefundingNumber(@Param("spuOrderId") Long spuOrderId, @Param("skuOrderIdList") List<Long> skuOrderIdList);

    /**
     * 构建 SPU 订单查询条件
     *
     * @param spuOrderQuery 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<SpuOrderDO> getLw(SpuOrderQuery spuOrderQuery) {
        BaseLambdaQueryWrapper<SpuOrderDO> wrapper = new BaseLambdaQueryWrapper<SpuOrderDO>()
                .notEmptyIn(SpuOrderDO::getId, spuOrderQuery.getIdList())
                .notEmptyIn(SpuOrderDO::getOrderId, spuOrderQuery.getOrderIdList())
                .notEmptyEq(SpuOrderDO::getOrderType, spuOrderQuery.getOrderType())
                .notEmptyIn(SpuOrderDO::getSpuId, spuOrderQuery.getSpuIdList())
                .notEmptyIn(SpuOrderDO::getChannelId, spuOrderQuery.getChannelIdList())
                .notEmptyIn(SpuOrderDO::getSupplierId, spuOrderQuery.getSupplierIdList())
                .notEmptyIn(SpuOrderDO::getMemberId, spuOrderQuery.getMemberIdList())
                .notEmptyEq(SpuOrderDO::getStoreId, spuOrderQuery.getStoreId())
                .notEmptyEq(SpuOrderDO::getDealerId, spuOrderQuery.getDealerId())
                .notEmptyEq(SpuOrderDO::getOutOrderNo, spuOrderQuery.getOutOrderNo())
                .notEmptyLike(SpuOrderDO::getSpuName, spuOrderQuery.getSpuName())
                .notEmptyLike(SpuOrderDO::getShipPhone, spuOrderQuery.getShipPhone())
                .notEmptyEq(SpuOrderDO::getSpuChannelType, spuOrderQuery.getSpuChannelType())
                .notEmptyIn(SpuOrderDO::getOrderState, spuOrderQuery.getOrderStateList())
                .notEmptyEq(SpuOrderDO::getRefund, spuOrderQuery.getRefund())
                .notEmptyEq(SpuOrderDO::getSettleSendState, spuOrderQuery.getSettleSendStatus())
                .betweenDate(SpuOrderDO::getCreateTime, spuOrderQuery.getCreateTime())
                ;
        wrapper.orderBy(spuOrderQuery);
        return wrapper;
    }
}