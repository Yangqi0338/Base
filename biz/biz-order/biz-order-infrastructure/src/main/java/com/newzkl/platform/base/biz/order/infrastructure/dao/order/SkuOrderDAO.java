package com.newzkl.platform.base.biz.order.infrastructure.dao.order;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SkuOrderDO;

import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.dto.SkuRefundDTO;
import com.newzkl.platform.base.biz.order.model.res.OrderRefundRes;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
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
     * @param orderNo 交易单号
     * @param skuId
     * @param count
     * @return
     */
    int updateSkuDeliverCount(@Param("orderNo") String orderNo, @Param("skuId") Long skuId, @Param("count") Integer count);
    /**
     * 获取已售后数量信息
     * @param wrapper
     * @return
     */
    List<SkuRefundDTO> skuRefundResList(@Param(Constants.WRAPPER) AbstractWrapper<?, ?, ?> wrapper);

    /**
     * SPU 维度售后统计
     *
     * <p>迁移自旧 SpuOrderDAO.spuRefundResList: SpuOrder 层折叠后按 order_no + spu_id 聚合 sku_order,
     * 运费取交易单 freight_amount</p>
     *
     * @param wrapper 查询包装器(别名 t)
     * @return SPU 维度售后统计列表
     */
    List<OrderRefundRes> orderRefundResList(@Param(Constants.WRAPPER) AbstractWrapper<?, ?, ?> wrapper);

    /**
     * 修改SKU售后数量
     * @param orderNo 交易单号
     * @param skuId
     * @param count
     * @return
     */
    int updateSkuRefundingCount(@Param("orderNo") String orderNo, @Param("skuId") Long skuId, @Param("count") Integer count);

    /**
     * SKU售后通过订单处理
     *         //前提: sku订单只能串行售后
     *         //将售后中数量移位至已售后数量
     *         //如果已售后数量 = 购买数量, 则关闭订单
     * @param skuIdList
     */
    int skuOrderEditForRefundPass(@Param("skuOrderNo") List<String> skuOrderNoList, @Param("toState") OrderEnum.State toState);

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
                .notEmptyIn(SkuOrderDO::getSpuId, skuOrderQuery.getSpuIdList())
                .notEmptyIn(SkuOrderDO::getSkuId, skuOrderQuery.getSkuIdList())
                .notEmptyIn(SkuOrderDO::getOrderNo, skuOrderQuery.getOrderNoList())
                .notEmptyIn(SkuOrderDO::getSkuOrderNo, skuOrderQuery.getSkuOrderNoList())
                .notEmptyIn(SkuOrderDO::getOrderState, skuOrderQuery.getOrderStateList())
                .notEmptyLt(SkuOrderDO::getDeliveredTime, skuOrderQuery.getLessDeliverTime())
                .notEmptyLt(SkuOrderDO::getReceiveTime, skuOrderQuery.getLessReceiveTime())
                .notEmptyEq(SkuOrderDO::getRefundingCount, skuOrderQuery.getRefundingCount())
                .notEmptyEq(SkuOrderDO::getSettleSendState, skuOrderQuery.getSettleSendState());
    }
}