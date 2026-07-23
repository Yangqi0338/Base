package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuAttributeDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuAttributeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* spu属性
* @author fang
*/
@Mapper
@Repository
public interface SpuAttributeDAO extends BaseMapper<SpuAttributeDO> {

    default QueryWrapper<SpuAttributeDO> buildQueryWrapper(SpuAttributeQuery query) {
        QueryWrapper<SpuAttributeDO> wrapper = new QueryWrapper<>();
        // idList 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getIdList()), "id", query.getIdList());

        // spuIdList 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getSpuIdList()), "spu_id", query.getSpuIdList());

        // type 条件
        wrapper.eq(query.getType() != null, "type", query.getType());
        return wrapper;
    }
}