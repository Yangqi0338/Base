package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.LevelDO;
import com.newzkl.platform.base.biz.account.model.level.req.LevelQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 等级 DAO。
 *
 * @author KC
 */
@Mapper
public interface LevelDAO extends BaseMapper<LevelDO> {

    /**
     * 构建等级查询条件。
     *
     * @param query 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<LevelDO> getLw(LevelQuery query) {
        BaseLambdaQueryWrapper<LevelDO> wrapper = new BaseLambdaQueryWrapper<>();
        wrapper.notEmptyIn(LevelDO::getId, query.getIdList())
                .notNullEq(LevelDO::getType, query.getType())
                .notNullEq(LevelDO::getValue, query.getValue());
        wrapper.orderByAsc(LevelDO::getValue);
        return wrapper;
    }
}
