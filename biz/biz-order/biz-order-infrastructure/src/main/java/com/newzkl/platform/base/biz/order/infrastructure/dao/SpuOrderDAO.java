package com.newzkl.platform.base.biz.order.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SpuOrderDO;
import com.newzkl.platform.base.biz.order.model.order.res.OrderStateCheckRes;
import com.newzkl.platform.base.biz.order.model.order.res.SpuRefundRes;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateCountVO;

import java.util.List;
import java.util.Map;

/**
 * SPU订单明细表 Mapper
 *
 * @author sijiwang
 */
@Mapper
public interface SpuOrderDAO extends BaseMapper<SpuOrderDO> {
    /**
     * 根据订单号更新SPU订单状态
     */
    int updateSpuOrderStateByOrderNo(@Param("orderNo") String orderNo, @Param("sourceState") Integer sourceState, @Param("toState") Integer toState, @Param("closeReason") String closeReason);

    /**
     * 统计指定渠道的订单状态数量
     */
    List<OrderStateCountVO> countOrderStateByChannel(@Param("channelId") Long channelId);

    /**
     * 统计指定账户的订单状态数量
     */
    List<OrderStateCountVO> countOrderStateByAccount(@Param("accountId") Long accountId);

    /**
     * 更新SPU订单售后数量
     */
    void updateSkuRefundingCount(@Param("spuOrderNo") String spuOrderNo,  @Param("refundQuantity") Integer refundQuantity);

    List<SpuRefundRes> spuRefundResList(@Param("orderNo") String orderNo, @Param("spuIds") List<Long> spuIds);

    void cutSkuOrderRefundingNumber(@Param("spuOrderNo") String spuOrderId, @Param("skuOrderNoList") List<String> skuOrderNoList);

    List<OrderStateCheckRes> checkOrderState(@Param("orderNos") List<String> orderNos);

    /**
     * 按条件包装器分组统计各订单状态的 SPU 订单数
     *
     * <p>对等旧 {@code SpuOrderDAO.countMapByQuery} + {@code groupField=t.order_state}。
     * 自定义 SQL 不走 MyBatis-Plus 逻辑删除注入, 逻辑删除条件由调用方在 wrapper 内显式给出。</p>
     *
     * @param wrapper 条件包装器 (不得含 order by)
     * @return 每行含 {@code orderState} / {@code orderCount} 两列
     */
    List<Map<String, Object>> stateCountMap(@Param(Constants.WRAPPER) Wrapper<SpuOrderDO> wrapper);
}