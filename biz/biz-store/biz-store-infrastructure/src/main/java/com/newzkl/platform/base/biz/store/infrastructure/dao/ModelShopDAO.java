package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
     * @param modelShop 样板店查询对象
     * @return BaseLambdaQueryWrapper查询包装器
     */
    default BaseLambdaQueryWrapper<ModelShopDO> buildQueryWrapper(ModelShopDO modelShop) {
        if (modelShop == null) {
            return new BaseLambdaQueryWrapper<>();
        }

        BaseLambdaQueryWrapper<ModelShopDO> wrapper = new BaseLambdaQueryWrapper<>();
        wrapper.notNullEq(ModelShopDO::getId, modelShop.getId());
        wrapper.notNullEq(ModelShopDO::getChannelId, modelShop.getChannelId());
        wrapper.notEmptyLike(ModelShopDO::getModelShopName, modelShop.getModelShopName());
        wrapper.notNullEq(ModelShopDO::getOperatorId, modelShop.getOperatorId());
        wrapper.notNullEq(ModelShopDO::getAuditState, modelShop.getAuditState());
        wrapper.notEmptyEq(ModelShopDO::getStyleCode, modelShop.getStyleCode());
        wrapper.notNullEq(ModelShopDO::getCreateId, modelShop.getCreateId());
        wrapper.notNullEq(ModelShopDO::getState, modelShop.getState());
        wrapper.notEmptyLike(ModelShopDO::getCreateName, modelShop.getCreateName());
        return wrapper;
    }
}