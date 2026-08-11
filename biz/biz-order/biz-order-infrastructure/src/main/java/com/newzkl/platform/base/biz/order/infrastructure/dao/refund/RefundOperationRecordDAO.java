package com.newzkl.platform.base.biz.order.infrastructure.dao.refund;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundOperationRecordDO;
import com.newzkl.platform.base.biz.order.model.req.query.RefundOperationRecordQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 售后操作记录表 Mapper 接口
 *
 * @author 开发者名称
 * @since 2026-01-23
 */
@Mapper
public interface RefundOperationRecordDAO extends BaseMapper<RefundOperationRecordDO> {

    default BaseLambdaQueryWrapper<RefundOperationRecordDO> getLw(RefundOperationRecordQuery query){
        return new BaseLambdaQueryWrapper<RefundOperationRecordDO>()
                .notEmptyEq(RefundOperationRecordDO::getRefundId, query.getRefundId())
                .notEmptyEq(RefundOperationRecordDO::getSpuOrderId, query.getSpuOrderId())
                .notEmptyEq(RefundOperationRecordDO::getOperatorId, query.getOperatorId())
                .notEmptyEq(RefundOperationRecordDO::getOperatorRoleCode, query.getOperatorRoleCode())
                ;
    }
}