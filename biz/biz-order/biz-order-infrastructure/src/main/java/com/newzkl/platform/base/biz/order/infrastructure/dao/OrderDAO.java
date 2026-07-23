package com.newzkl.platform.base.biz.order.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.OrderDO;

/**
 * 交易订单主表 Mapper
 *
 * @author sijiwang
 */
@Mapper
public interface OrderDAO extends BaseMapper<OrderDO> {

    int updateOrderStateByOrderNo(@Param("orderNo") String orderNo, @Param("sourceState") Integer sourceState, @Param("toState") Integer toState, @Param("closeReason") String closeReason);
}