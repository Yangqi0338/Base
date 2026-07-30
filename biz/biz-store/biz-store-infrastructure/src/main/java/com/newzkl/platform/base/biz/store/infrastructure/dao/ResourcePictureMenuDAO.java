package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.newzkl.platform.base.biz.store.infrastructure.entity.ResourcePictureMenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ResourcePictureMenuDAO继承基类
 */
@Mapper
@Repository
public interface ResourcePictureMenuDAO extends com.baomidou.mybatisplus.core.mapper.BaseMapper<ResourcePictureMenuDO> {
}
