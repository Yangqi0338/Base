package com.newzkl.platform.base.biz.order.infrastructure.dao.order;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.OrderDO;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.dto.OrderStateCheckDTO;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 订单
* @author fang
*/
@Mapper
public interface OrderDAO extends BaseMapper<OrderDO> {

    List<OrderStateCheckDTO> checkOrderState(@Param("orderId") List<Long> orderId);

    /**
     * 构建交易单查询条件
     *
     * @param orderQuery 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<OrderDO> getLw(OrderQuery orderQuery) {
        BaseLambdaQueryWrapper<OrderDO> wrapper = new BaseLambdaQueryWrapper<OrderDO>()
                .notEmptyIn(OrderDO::getId, orderQuery.getIdList())
                .notEmptyIn(OrderDO::getAccountId, orderQuery.getAccountIdList())
                .notEmptyEq(OrderDO::getOrderType, orderQuery.getOrderType())
                .notEmptyEq(OrderDO::getOperatorId, orderQuery.getOperatorId())
                .notEmptyEq(OrderDO::getChannelId, orderQuery.getChannelId())
                .notEmptyEq(OrderDO::getMemberId, orderQuery.getMemberId())
                .notEmptyIn(OrderDO::getOutOrderNo, orderQuery.getOutOrderNoList())
                .notEmptyIn(OrderDO::getOrderState, orderQuery.getOrderStateList())
                .between(OrderDO::getCreateTime, orderQuery.getCreateTime());
        return wrapper;
    }
}