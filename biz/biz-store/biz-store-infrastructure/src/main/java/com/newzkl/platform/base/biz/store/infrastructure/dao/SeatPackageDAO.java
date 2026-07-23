package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.store.infrastructure.entity.SeatPackageDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 席位套餐表Mapper接口
 */
@Mapper
public interface SeatPackageDAO extends BaseMapper<SeatPackageDO> {

}