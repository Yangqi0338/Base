package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreStyleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 门店样式表Mapper接口
 */
@Mapper
public interface StoreStyleDAO extends BaseMapper<StoreStyleDO> {

}