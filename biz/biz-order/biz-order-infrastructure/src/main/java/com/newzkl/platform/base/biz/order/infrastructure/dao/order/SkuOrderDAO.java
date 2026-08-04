package com.newzkl.platform.base.biz.order.infrastructure.dao.order;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SkuOrderDO;

import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.dto.SkuRefundDTO;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
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
    /**
     * 修改SKU发货数量
     * @param spuOrderId
     * @param skuId
     * @param count
     * @return
     */
    int updateSkuDeliverCount(@Param("spuOrderId") Long spuOrderId, @Param("skuId") Long skuId, @Param("count") Integer count);
    /**
     * 获取已售后数量信息
     * @param wrapper
     * @return
     */
    List<SkuRefundDTO> skuRefundResList(@Param(Constants.WRAPPER) AbstractWrapper<?, ?, ?> wrapper);

    /**
     * 修改SKU售后数量
     * @param orderId
     * @param skuId
     * @param count
     * @return
     */
    int updateSkuRefundingCount(@Param("orderId") Long orderId, @Param("skuId") Long skuId, @Param("count") Integer count);

    /**
     * SKU售后通过订单处理
     *         //前提: sku订单只能串行售后
     *         //将售后中数量移位至已售后数量
     *         //如果已售后数量 = 购买数量, 则关闭订单
     * @param skuIdList
     */
    int skuOrderEditForRefundPass(@Param("skuOrderId") List<Long> skuIdList, @Param("toState") OrderEnum.State toState);

    /**
     * 构建 SKU 订单查询条件
     *
     * <p>迁移自旧 SkuOrderDAO.xml {@code <sql id="where">}: 条件收敛至 LambdaQueryWrapper, 去除 mapper 内 {@code <where>} 标签。
     * del_flag 由 {@code @TableLogic} 自动追加。</p>
     *
     * @param skuOrderQuery 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<SkuOrderDO> getLw(SkuOrderQuery skuOrderQuery) {
        return new BaseLambdaQueryWrapper<SkuOrderDO>()
                .notEmptyIn(SkuOrderDO::getId, skuOrderQuery.getIdList())
                .notEmptyIn(SkuOrderDO::getSpuOrderId, skuOrderQuery.getSpuOrderIdList())
                .notEmptyIn(SkuOrderDO::getSkuId, skuOrderQuery.getSkuIdList())
                .notEmptyIn(SkuOrderDO::getOrderId, skuOrderQuery.getOrderIdList())
                .notEmptyIn(SkuOrderDO::getOrderState, skuOrderQuery.getOrderStateList())
                .notEmptyLt(SkuOrderDO::getDeliveredTime, skuOrderQuery.getLessDeliverTime())
                .notEmptyLt(SkuOrderDO::getReceiveTime, skuOrderQuery.getLessReceiveTime())
                .notEmptyEq(SkuOrderDO::getRefundingCount, skuOrderQuery.getRefundingCount())
                .notEmptyEq(SkuOrderDO::getSettleSendState, skuOrderQuery.getSettleSendState());
    }
}