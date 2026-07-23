package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreZoneDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 门店专区表DAO接口
 */
@Mapper
public interface StoreZoneDAO extends BaseMapper<StoreZoneDO> {
}