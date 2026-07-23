package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.GoodsZoneDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 商品分组Mapper接口
 * 提供商品分组相关的数据库操作方法（基础增删改查复用BaseMapper）
 * @author sijiwang
 */
@Mapper
@Repository
public interface GoodsZoneDAO extends BaseMapper<GoodsZoneDO> {

}