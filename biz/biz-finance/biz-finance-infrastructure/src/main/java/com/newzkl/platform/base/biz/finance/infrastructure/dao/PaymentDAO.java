package com.newzkl.platform.base.biz.finance.infrastructure.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.PaymentDO;
import com.newzkl.platform.base.biz.finance.model.pay.req.PaymentQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * PaymentDAO继承基类
 */
@Mapper
public interface PaymentDAO extends BaseMapper<PaymentDO> {

    default LambdaQueryWrapper<PaymentDO> getLw(PaymentQuery query) {
        return new BaseQueryWrapper<PaymentDO>()
                .jsonEq("order_info", "level", false, query.getLevel())
                .lambda()
                .notEmptyEq(PaymentDO::getAccountId, query.getAccountId())
                .notEmptyEq(PaymentDO::getPayState, query.getPayState())
                .notEmptyIn(PaymentDO::getTradeNo, query.getTradeNoList())
                .notEmptyEq(PaymentDO::getOrderNo, query.getOrderNoList())
                .notEmptyEq(PaymentDO::getConsumeType, query.getConsumeType())
                ;
    }

}
