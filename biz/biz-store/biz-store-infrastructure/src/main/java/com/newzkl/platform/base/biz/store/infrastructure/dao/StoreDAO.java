package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreDO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreStyleDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 门店
* @author fang
*/
@Mapper
@Repository
public interface StoreDAO extends BaseMapper<StoreDO> {

    /**
     * 构建StoreDO的Lambda查询条件
     * 支持所有字段的动态查询
     *
     * @param storeDO 门店查询对象
     * @return BaseLambdaQueryWrapper查询包装器
     */
    default BaseLambdaQueryWrapper<StoreDO> buildQueryWrapper(StoreDO storeDO) {
        if (storeDO == null) {
            return new BaseLambdaQueryWrapper<>();
        }

        BaseLambdaQueryWrapper<StoreDO> wrapper = new BaseLambdaQueryWrapper<>();
        wrapper.notNullEq(StoreDO::getId, storeDO.getId());
        wrapper.notNullEq(StoreDO::getChannelId, storeDO.getChannelId());
        wrapper.notEmptyLike(StoreDO::getName, storeDO.getName());
        wrapper.notNullEq(StoreDO::getMerchantId, storeDO.getMerchantId());
        wrapper.notNullEq(StoreDO::getManagerId, storeDO.getManagerId());
        wrapper.notNullEq(StoreDO::getStyleCode, storeDO.getStyleCode());
        wrapper.notNullEq(StoreDO::getModelShopId, storeDO.getModelShopId());
        wrapper.notNullEq(StoreDO::getIsModelShop, storeDO.getIsModelShop());
        wrapper.notNullEq(StoreDO::getType, storeDO.getType());
        return wrapper;
    }

}