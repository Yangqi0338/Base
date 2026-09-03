package com.newzkl.platform.base.biz.order.infrastructure.dao.order;


import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.newzkl.platform.base.biz.order.infrastructure.entity.OrderDO;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.dto.OrderStateCheckDTO;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 订单
* @author fang
*/
@Mapper
public interface OrderDAO extends BaseMapper<OrderDO> {

    List<OrderStateCheckDTO> checkOrderState(@Param("orderNoList") List<String> orderNoList);

    /**
     * 按查询分组统计订单状态数量(SpuOrder 层折叠: 迁自 SpuOrderDAO.countMapWithOrderByQuery, 数据源 spu_order 改 order 表)
     *
     * @param wrapper MP 条件包装器
     * @param query   订单查询条件(携带 fieldSQL/groupSQL)
     * @return 分组计数
     */
    BizCountMap countMapWithOrderByQuery(@Param(Constants.WRAPPER) AbstractWrapper<?, ?, ?> wrapper, @Param("query") OrderQuery query);

    /**
     * 按时间分组统计订单数与金额(SpuOrder 层折叠: 迁自 SpuOrderDAO.orderCount, 数据源 spu_order 改 order 表)
     *
     * @param timeQuery 时间区间查询
     * @return 分组统计
     */
    List<GroupCountRes> orderCount(@Param("query") TimeQuery timeQuery);

    /**
     * 构建交易单查询条件
     *
     * @param orderQuery 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<OrderDO> getLw(OrderQuery orderQuery) {
        BaseLambdaQueryWrapper<OrderDO> wrapper = new BaseLambdaQueryWrapper<OrderDO>()
                .notEmptyIn(OrderDO::getId, orderQuery.getIdList())
                .notEmptyEq(OrderDO::getOrderType, orderQuery.getOrderType())
                .notEmptyEq(OrderDO::getChannelId, orderQuery.getChannelId())
                .notEmptyIn(OrderDO::getChannelId, orderQuery.getChannelIdList())
                .notEmptyEq(OrderDO::getMemberId, orderQuery.getMemberId())
                .notEmptyIn(OrderDO::getMemberId, orderQuery.getMemberIdList())
                .notEmptyIn(OrderDO::getId, orderQuery.getOrderIdList())
                .notEmptyIn(OrderDO::getOrderNo, orderQuery.getOrderNoList())
                .notEmptyEq(OrderDO::getStoreId, orderQuery.getStoreId())
                .notEmptyIn(OrderDO::getOutOrderNo, orderQuery.getOutOrderNoList())
                .notEmptyIn(OrderDO::getOrderState, orderQuery.getOrderStateList())
                .between(OrderDO::getCreateTime, orderQuery.getCreateTime())
                .notEmptyLt(OrderDO::getCreateTime, orderQuery.getLessCreateTime());
        return wrapper;
    }

    default BaseLambdaQueryWrapper<OrderDO> getKeyLw(String orderNo){
        BaseLambdaQueryWrapper<OrderDO> wrapper = new BaseLambdaQueryWrapper<OrderDO>()
                .notEmptyEq(OrderDO::getOrderNo, orderNo);
        return wrapper;
    }
}