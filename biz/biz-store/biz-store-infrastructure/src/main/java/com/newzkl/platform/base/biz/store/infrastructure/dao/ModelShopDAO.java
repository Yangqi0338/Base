package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.store.model.template.req.ModelShopQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.biz.store.infrastructure.entity.ModelShopDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ModelShopDAO继承基类
 */
@Mapper
@Repository
public interface ModelShopDAO extends BaseMapper<ModelShopDO> {

    /**
     * 构建ModelShopDO的Lambda查询条件
     * 支持所有字段的动态查询
     *
     * @param query 样板店查询对象
     * @return BaseLambdaQueryWrapper查询包装器
     */
    default BaseLambdaQueryWrapper<ModelShopDO> getLw(ModelShopQuery query) {
        if (query == null) {
            return new BaseLambdaQueryWrapper<>();
        }

        BaseLambdaQueryWrapper<ModelShopDO> wrapper = new BaseLambdaQueryWrapper<>();
        wrapper.notNullEq(ModelShopDO::getId, query.getId());
        wrapper.notNullEq(ModelShopDO::getChannelId, query.getChannelId());
        wrapper.notEmptyLike(ModelShopDO::getModelShopName, query.getModelShopName());
        wrapper.notNullEq(ModelShopDO::getOperatorId, query.getOperatorId());
        wrapper.notNullEq(ModelShopDO::getChannelId, query.getChannelId());
        wrapper.notNullEq(ModelShopDO::getAuditState, query.getAuditState());
        wrapper.notEmptyEq(ModelShopDO::getStyleCode, query.getStyleCode());
        wrapper.notNullEq(ModelShopDO::getCreatorId, query.getCreatorId());
        wrapper.notNullEq(ModelShopDO::getState, query.getState());
        wrapper.between(ModelShopDO::getCreateTime, query.getCreateTime());
        wrapper.orderByDesc(ModelShopDO::getId);
        return wrapper;
    }
}