package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuAttributeDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuAttributeQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* spu属性
* @author fang
*/
@Mapper
@Repository
public interface SpuAttributeDAO extends BaseMapper<SpuAttributeDO> {

    /**
     * 构建 SPU 属性通用条件包装器
     *
     * @param query SPU 属性查询, 可为 null
     * @return 条件包装器, 恒非 null
     */
    default BaseLambdaQueryWrapper<SpuAttributeDO> getLw(SpuAttributeQuery query) {
        BaseLambdaQueryWrapper<SpuAttributeDO> wrapper = new BaseLambdaQueryWrapper<>(SpuAttributeDO.class);
        if (query == null) {
            return wrapper;
        }
        return wrapper.notEmptyIn(SpuAttributeDO::getId, query.getIdList())
                .notEmptyIn(SpuAttributeDO::getSpuId, query.getSpuIdList())
                .notEmptyEq(SpuAttributeDO::getType, query.getType());
    }
}
