package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.newzkl.platform.base.biz.store.infrastructure.entity.FitmentPageDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * FitmentPageDAO继承基类
 */
@Mapper
@Repository
public interface FitmentPageDAO extends com.baomidou.mybatisplus.core.mapper.BaseMapper<FitmentPageDO> {
}
