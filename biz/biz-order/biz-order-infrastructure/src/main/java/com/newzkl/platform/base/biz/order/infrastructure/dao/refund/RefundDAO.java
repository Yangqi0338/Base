package com.newzkl.platform.base.biz.order.infrastructure.dao.refund;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundDO;
import com.newzkl.platform.base.biz.order.model.req.query.RefundQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
* 售后单
* @author fang
*/
@Mapper
public interface RefundDAO extends BaseMapper<RefundDO> {

    default BaseLambdaQueryWrapper<RefundDO> getLw(RefundQuery query){
        return new BaseLambdaQueryWrapper<RefundDO>()
                .notEmptyIn(RefundDO::getId, query.getIdList())
                .notEmptyEq(RefundDO::getSpuOrderId, query.getSpuOrderId())
                .notEmptyEq(RefundDO::getMemberId, query.getMemberId())
                .notEmptyEq(RefundDO::getOrderType, query.getOrderType())
                .notEmptyEq(RefundDO::getRefundType, query.getRefundType())
                .notEmptyIn(RefundDO::getRefundState, query.getRefundStateList())
                .notEmptyEq(RefundDO::getSupplierId, query.getSupplierId())
                .notEmptyEq(RefundDO::getChannelId, query.getChannelId())
                .notEmptyLt(RefundDO::getStateTime, query.getStateTimeLess())
                .notEmptyNotIn(RefundDO::getFromOrderState, query.getFromOrderStateNot())
                .between(RefundDO::getCreateTime, query.getCreateTime())
                .jsonLike(RefundDO::getItem, "spuName",query.getSpuName())
                .jsonEq(RefundDO::getItem, "spuId",query.getSpuId())
                ;
    }
}