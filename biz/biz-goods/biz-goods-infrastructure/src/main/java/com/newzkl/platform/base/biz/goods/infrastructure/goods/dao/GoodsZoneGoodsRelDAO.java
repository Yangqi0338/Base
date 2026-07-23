package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.GoodsZoneGoodsRelDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 商品分组-商品关联Mapper接口
 * @author sijiwang
 */
@Mapper
@Repository
public interface GoodsZoneGoodsRelDAO extends BaseMapper<GoodsZoneGoodsRelDO> {

}