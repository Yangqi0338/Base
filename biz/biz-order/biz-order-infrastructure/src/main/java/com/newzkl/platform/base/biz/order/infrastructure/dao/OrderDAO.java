package com.newzkl.platform.base.biz.order.infrastructure.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.OrderDO;
import com.newzkl.platform.base.biz.order.model.res.OrderStateCheckRes;
import com.newzkl.platform.base.biz.order.model.vo.OrderPayVO;
import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 订单
* @author fang
*/
@Mapper
public interface OrderDAO extends BaseMapper<OrderDO> {

    int batchUpdateOrderState(@Param("orderIdList") List<Long> orderIdList, @Param("sourceState") OrderEnum.State sourceState, @Param("toState") OrderEnum.State toState);

    List<OrderStateCheckRes> checkOrderState(@Param("orderId") List<Long> orderId);

    void updateOrderShip(@Param("orderId")Long orderId, @Param("shipVo")String shipVo);
}