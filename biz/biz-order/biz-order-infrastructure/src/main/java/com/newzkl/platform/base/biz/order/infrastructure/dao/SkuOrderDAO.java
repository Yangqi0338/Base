package com.newzkl.platform.base.biz.order.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderAmountVO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SkuOrderDO;

import com.newzkl.platform.base.biz.order.model.req.SkuOrderCommand;
import com.newzkl.platform.base.biz.order.model.req.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.res.AlreadyDeliverRes;
import com.newzkl.platform.base.biz.order.model.res.SkuRefundRes;
import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderItemExcelVO;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* SKU订单
* @author fang
*/
@Mapper
@Repository
public interface SkuOrderDAO extends BaseMapper<SkuOrderDO> {
    int batchUpdateOrderStateByOrderId(@Param("orderIdList") List<Long> orderIdList, @Param("sourceState") OrderEnum.State sourceState, @Param("toState") OrderEnum.State toState);

    List<SkuOrderDO> listDOByQuery(@Param("query") SkuOrderQuery skuOrderQuery);

    int batchUpdateSkuOrderState(@Param("skuOrderIdList") List<Long> skuOrderIdList, @Param("sourceState") OrderEnum.State sourceState, @Param("toState") OrderEnum.State toState, @Param("command") SkuOrderCommand command);
    /**
     * 修改SKU发货数量
     * @param spuOrderId
     * @param skuId
     * @param count
     * @return
     */
    int updateSkuDeliverCount(@Param("spuOrderId") Long spuOrderId, @Param("skuId") Long skuId, @Param("count") Integer count);
    /**
     * 获取已发货数量信息
     * @param spuOrderId
     * @param skuIds
     * @return
     */
    List<AlreadyDeliverRes> getAlreadyDeliverResList(@Param("spuOrderId") Long spuOrderId, @Param("skuIds") List<Long> skuIds);
    /**
     * 获取已售后数量信息
     * @param orderId
     * @param skuIds
     * @return
     */
    List<SkuRefundRes> skuRefundResList(@Param("orderId") Long orderId, @Param("skuIds") List<Long> skuIds);
    /**
     * 修改SKU售后数量
     * @param orderId
     * @param skuId
     * @param count
     * @return
     */
    int updateSkuRefundingCount(@Param("orderId") Long orderId, @Param("skuId") Long skuId, @Param("count") Integer count);

    List<Long> orderIdBySpuSkuOrderId(@Param("spuOrderId") List<Long> spuOrderId, @Param("skuOrderId") List<Long> skuOrderId);

    /**
     * SKU售后通过订单处理
     *         //前提: sku订单只能串行售后
     *         //将售后中数量移位至已售后数量
     *         //如果已售后数量 = 购买数量, 则关闭订单
     * @param skuIdList
     */
    void skuOrderEditForRefundPass(@Param("skuOrderId") List<Long> skuIdList);
    /**
     * SKU售后关闭订单处理
     * @param skuIdList
     */
    void skuOrderEditForRefundClose(@Param("skuOrderId") List<Long> skuIdList);

    List<Long> idByQuery(@Param("query") SkuOrderQuery skuOrderQuery);

    List<SkuOrderVO> querySkuOrderByOrderId(@Param("orderId") Long orderId, @Param("skuIdList") List<Long> skuIdList);
}