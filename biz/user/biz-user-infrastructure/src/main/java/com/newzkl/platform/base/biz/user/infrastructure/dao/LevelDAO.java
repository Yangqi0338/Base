package com.newzkl.platform.base.biz.user.infrastructure.dao;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.po.LevelDO;
import com.newzkl.platform.base.biz.user.model.relation.req.LevelQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * 等级 Mapper。
 *
 * <p>迁移说明：源 getLw 使用 common BaseLambdaQueryWrapper(notEmptyEq/notEmptyIn)，
 * 新 common 无此基类，改用原生 MP LambdaQueryWrapper。LevelDO 无 value 字段，
 * 源 value 条件不再适用，已省略。</p>
 *
 * @author fang
 */
@Mapper
public interface LevelDAO extends BaseMapper<LevelDO> {

    /**
     * 构造等级查询包装器。
     *
     * @param query 查询条件
     * @return LambdaQueryWrapper
     */
    default LambdaQueryWrapper<LevelDO> getLw(LevelQuery query) {
        LambdaQueryWrapper<LevelDO> lw = new LambdaQueryWrapper<>();
        lw.eq(query.getType() != null, LevelDO::getType, query.getType());
        lw.in(CollUtil.isNotEmpty(query.getIdList()), LevelDO::getId, query.getIdList());
        return lw;
    }

}
