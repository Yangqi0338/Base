package com.newzkl.platform.base.biz.order.infrastructure.dao.settle;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleOrderWaitDO;
import com.newzkl.platform.base.biz.order.model.req.query.SettleOrderWaitQuery;
import com.newzkl.platform.base.biz.order.model.vo.SettleOrderWaitVO;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
* 待结算订单信息表
* @author fang
*/
@Mapper
public interface SettleOrderWaitDAO extends BaseMapper<SettleOrderWaitDO> {



    /**
     * 查询商品待结算
     * @param spuIds
     * @return
     */
    List<SettleOrderWaitVO> queryWaitSettleOrder(@Param("spuIdList") List<Long> spuIds);

    List<SettleOrderWaitVO> queryWaitSettleOrderIn(@Param("spuIdList") List<Long> spuIds, @Param("skuOrderId") List<Long> skuOrderId);

    List<SettleOrderWaitVO> queryWaitSettleOrderTimeNode(Long settleTimeNode);

    default BaseLambdaQueryWrapper<SettleOrderWaitDO> getLw(SettleOrderWaitQuery query){
        return new BaseLambdaQueryWrapper<SettleOrderWaitDO>()
                .notEmptyIn(SettleOrderWaitDO::getId, query.getIdList())
                .notEmptyIn(SettleOrderWaitDO::getSpuId, query.getSpuIdList())
                .notNullEq(SettleOrderWaitDO::getSupplierId, query.getSupplierId())
                .notNullEq(SettleOrderWaitDO::getSettleState, query.getSettleState())
                .notNullEq(SettleOrderWaitDO::getSkuOrderId, query.getSkuOrderId())
                .notNullEq(SettleOrderWaitDO::getType, query.getType())
                .notNullEq(SettleOrderWaitDO::getSpuOrderId, query.getSpuOrderId())
                ;
    }
}