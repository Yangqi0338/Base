package com.newzkl.platform.base.biz.order.infrastructure.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleOrderWaitDO;
import com.newzkl.platform.base.biz.order.model.vo.SettleOrderWaitVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
* 待结算订单信息表
* @author fang
*/
@Mapper
public interface SettleOrderWaitDAO extends BaseMapper<SettleOrderWaitDO> {

    /**
     * 待结算订单修改 for 执行结算
     */
    void settleOrderWaitEditForExecuteSettle(@Param("settleOrderWaitIdList") List<Long> settleOrderWaitIdList, @Param("settleState") Integer settleState, @Param("settleTime") LocalDateTime settleTime, @Param("settleRecordId") Long settleRecordId);

    /**
     * 查询商品待结算
     * @param spuIds
     * @return
     */
    List<SettleOrderWaitVO> queryWaitSettleOrder(@Param("spuIdList") List<Long> spuIds);

    /**
     * 统计
     * @param skuOrderId
     * @return
     */
    Long idBySkuOrderIdAndType(@Param("skuOrderId") Long skuOrderId, @Param("type") Integer type);

    List<SettleOrderWaitVO> queryWaitSettleOrderIn(@Param("spuIdList") List<Long> spuIds, @Param("skuOrderId") List<Long> skuOrderId);

    Integer editRefundState(@Param("id") Long id, @Param("sourceState") int sourceSettleState, @Param("refundState") int refundState, @Param("refundId") Long refundId);

    void alterWaitSettleFreightTimeNode( @Param("spuOrderId")Long spuOrderId, @Param("settleNodeTime")Long settleNodeTime);

    List<SettleOrderWaitVO> queryWaitSettleOrderTimeNode(Long settleTimeNode);
}