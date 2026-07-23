package com.newzkl.platform.base.biz.order.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.OrderDeliveryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDeliveryDAO extends BaseMapper<OrderDeliveryDO> {
}