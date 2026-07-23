package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreZoneGoodsRelationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 门店专区商品关系表Mapper接口
 */
@Mapper
public interface StoreZoneGoodsRelationDAO extends BaseMapper<StoreZoneGoodsRelationDO> {

}