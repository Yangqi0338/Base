package com.newzkl.platform.base.biz.sys.infrastructure.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.DictDO;
import com.newzkl.platform.base.biz.sys.model.dict.query.DictQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典 DAO
 *
 * @author fang
 */
@Mapper
public interface DictDAO extends BaseMapper<DictDO> {

    /**
     * 构建字典查询条件
     *
     * @param dictQuery 字典查询
     * @return 查询条件
     */
    default LambdaQueryWrapper<DictDO> getLw(DictQuery dictQuery) {
        return new BaseLambdaQueryWrapper<DictDO>()
                .notEmptyIn(DictDO::getId, dictQuery.getIdList());
    }
}
