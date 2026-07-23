package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.CdkDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.virtualSpu.CdkQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * 兑换码
 */
@Mapper
public interface CdkDAO extends BaseMapper<CdkDO> {

    /**
     * 构建 CdkQuery 查询条件包装器
     */
    default LambdaQueryWrapper<CdkDO> buildQueryWrapper(CdkQuery query) {
        return new LambdaQueryWrapper<CdkDO>()
                .eq(query.getId() != null, CdkDO::getId, query.getId())
                .in(CollectionUtils.isNotEmpty(query.getIdList()), CdkDO::getId, query.getIdList())
                .eq(query.getBelowRole() != null, CdkDO::getBelowRole, query.getBelowRole())
                .eq(query.getOperatorId() != null, CdkDO::getOperatorId, query.getOperatorId())
                .eq(query.getDealerId() != null, CdkDO::getDealerId, query.getDealerId())
                .eq(query.getChannelId() != null, CdkDO::getChannelId, query.getChannelId())
                .eq(query.getValue() != null, CdkDO::getValue, query.getValue())
                .in(CollectionUtils.isNotEmpty(query.getValueList()), CdkDO::getValue, query.getValueList())
                .eq(query.getUseState() != null, CdkDO::getUseState, query.getUseState())
                .eq(query.getToState() != null, CdkDO::getToState, query.getToState())
                .eq(query.getSystemType() != null, CdkDO::getSystemType, query.getSystemType())
                .in(CollectionUtils.isNotEmpty(query.getSystemTypeList()), CdkDO::getSystemType, query.getSystemTypeList())
                .lt(query.getLessCreateTime() != null, CdkDO::getCreateTime, query.getLessCreateTime())
                .eq(query.getOrderId() != null, CdkDO::getOrderId, query.getOrderId())
                .eq(query.getGetType() != null, CdkDO::getGetType, query.getGetType())
                .orderByDesc(CdkDO::getId);
    }

}