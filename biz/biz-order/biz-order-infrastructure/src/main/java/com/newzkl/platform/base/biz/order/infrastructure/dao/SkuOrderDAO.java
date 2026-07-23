package com.newzkl.platform.base.biz.order.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.newzkl.platform.base.biz.order.model.order.res.OrderStateCheckRes;
import com.newzkl.platform.base.biz.order.model.order.res.SkuRefundRes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SkuOrderDO;

import java.util.List;

/**
 * SKU订单明细表 Mapper
 *
 * @author sijiwang
 */
@Mapper
public interface SkuOrderDAO extends BaseMapper<SkuOrderDO> {
    /**
     * 根据订单号更新SKU订单状态
     */
    int updateSkuOrderStateByOrderNo(@Param("orderNo") String orderNo, @Param("sourceState") Integer sourceState, @Param("toState") Integer toState, @Param("closeReason") String closeReason);

    /**
     * 修改SKU售后数量
     * @param spuOrderNo
     * @param skuId
     * @param count
     * @return
     */
    int updateSkuRefundingQuantity(@Param("spuOrderNo") String spuOrderNo, @Param("skuId") Long skuId, @Param("count") Integer count);

    /**
     * 获取已售后数量信息
     * @param orderNo
     * @param skuIds
     * @return
     */
    List<SkuRefundRes> skuRefundResList(@Param("orderNo") String orderNo, @Param("skuIds") List<Long> skuIds);

    /**
     * SKU售后关闭订单处理
     */
    void skuOrderEditForRefundClose(@Param("skuOrderNo") List<String> skuOrderNoList);

    /**
     * SKU售后通过订单处理
     */
    void skuOrderEditForRefundPass(@Param("skuOrderNo") List<String> skuOrderNoList);

    /**
     * 订单状态检查
     */
    List<OrderStateCheckRes> checkSpuOrderState(@Param("orderNos") List<String> orderNos);
}