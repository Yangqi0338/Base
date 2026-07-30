package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.newzkl.platform.base.biz.store.infrastructure.entity.FitmentTemplateDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * FitmentTemplateDAO继承基类
 */
@Mapper
@Repository
public interface FitmentTemplateDAO extends com.baomidou.mybatisplus.core.mapper.BaseMapper<FitmentTemplateDO> {
}
