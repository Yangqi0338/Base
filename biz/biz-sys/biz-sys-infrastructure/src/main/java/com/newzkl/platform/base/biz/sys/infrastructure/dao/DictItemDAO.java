package com.newzkl.platform.base.biz.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.DictItemDO;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典条目 DAO
 *
 * @author KC
 */
@Mapper
public interface DictItemDAO extends BaseMapper<DictItemDO> {

    /**
     * 构建按父字典 id 的查询条件 (按 sort 升序)
     *
     * @param dictId 父字典 id
     * @return 查询条件
     */
    default LambdaQueryWrapper<DictItemDO> getLwByDictId(Long dictId) {
        return new BaseLambdaQueryWrapper<DictItemDO>()
                .notNullEq(DictItemDO::getDictId, dictId)
                .orderByAsc(DictItemDO::getSort);
    }
}
