package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.newzkl.platform.base.biz.store.infrastructure.entity.ResourcePictureDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ResourcePictureDAO继承基类
 */
@Mapper
@Repository
public interface ResourcePictureDAO extends com.baomidou.mybatisplus.core.mapper.BaseMapper<ResourcePictureDO> {
}
