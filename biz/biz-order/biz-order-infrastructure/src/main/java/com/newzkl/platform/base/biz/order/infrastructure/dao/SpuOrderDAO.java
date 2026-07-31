package com.newzkl.platform.base.biz.order.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.facade.model.api.order.OrderSummaryReq;
import com.newzkl.platform.base.biz.order.facade.model.count.OrderSummaryVO;
import com.newzkl.platform.base.biz.order.facade.model.count.SaleCountVO;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderStateVO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SpuOrderDO;

import com.newzkl.platform.base.biz.order.model.req.SpuOrderQuery;
import com.newzkl.platform.base.biz.order.model.res.OrderStateCheckRes;
import com.newzkl.platform.base.biz.order.model.res.SpuRefundRes;
import com.newzkl.platform.base.biz.order.model.vo.CountDto;
import com.newzkl.platform.base.biz.order.model.vo.OrderStateCountVO;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* SPU订单
* @author fang
*/
@Mapper
@Repository
public interface SpuOrderDAO extends BaseMapper<SpuOrderDO> {

    List<Map<String, Object>> countMapByQuery(@Param("query") SpuOrderQuery query);

    int batchUpdateOrderStateByOrderId(@Param("orderIdList") List<Long> orderIdList, @Param("sourceState")  OrderEnum.State sourceState, @Param("toState")  OrderEnum.State toState, @Param("spuOrderExt") String spuOrderExt);

    List<SpuOrderDO> listDOByQuery(@Param("query") SpuOrderQuery spuOrderQuery);

    void batchUpdateSpuOrderState(@Param("spuOrderIdList") List<Long> spuOrderIdList, @Param("sourceState")  OrderEnum.State sourceState, @Param("toState")  OrderEnum.State toState,  @Param("model") SpuOrderDO spuOrderDO);

    List<SpuOrderStateVO> accountOrderState(@Param("accountId") Long accountId, @Param("spuOrderIdList") List<Long> spuOrderIdList);

    SpuOrderRelationVO spuOrderRelation(@Param("orderId") Long orderId, @Param("spuId") Long spuId);

    List<OrderStateCheckRes> checkSpuOrderState(@Param("orderId") List<Long> orderId);

    List<SpuRefundRes> spuRefundResList(@Param("orderId") Long orderId, @Param("spuIds") List<Long> spuIds);

    List<GroupCountRes> orderCount(@Param("query") TimeQuery timeQuery);

    Integer sumAmountByQuery(@Param("query") SpuOrderQuery spuOrderQuery);

    void updateSkuRefundingCount(@Param("spuOrderId") Long spuOrderId,  @Param("count") Integer count);

    void cutSkuOrderRefundingNumber(@Param("spuOrderId") Long spuOrderId, @Param("skuOrderIdList") List<Long> skuOrderIdList);

    Long spuOrderIdByorderIdSkuId(@Param("orderId") Long orderId, @Param("skuId") Long skuId);

    List<OrderSummaryVO> channelCountVO(@Param("query") OrderSummaryReq query);

    void updateOrderShip(@Param("orderId")Long orderId, @Param("shipVo")String shipVo);

    /**
     * 统计渠道商所有订单状态数量
     * @param channelId 渠道商ID
     * @return 订单状态统计列表
     */
    List<OrderStateCountVO> countOrderStateByChannel(@Param("channelId") Long channelId);

    /**
     * 统计会员所有订单状态数量
     * @param accountId 会员ID
     * @return 订单状态统计列表
     */
    List<OrderStateCountVO> countOrderStateByAccount(@Param("accountId") Long accountId);

    /**
     * 根据订单状态和最后修改时间（小于指定时间）查询订单数据
     * @param orderState 订单状态（必填）
     * @param updateTime 最后修改时间阈值（必填，查询 update_time < 该时间的数据）
     * @return 符合条件的 SpuOrderDO 列表
     */
    List<SpuOrderDO> listDOByOrderStateAndUpdateTimeLessThan(@Param("orderState") OrderEnum.State orderState, @Param("updateTime") LocalDateTime updateTime);
}