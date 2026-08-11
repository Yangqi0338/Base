package com.newzkl.platform.base.biz.user.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.entity.PackGoodsDO;
import com.newzkl.platform.base.biz.user.model.pack.query.PackGoodsQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入会礼包商品 Mapper
 *
 * @author KC
 */
@Mapper
public interface PackGoodsDAO extends BaseMapper<PackGoodsDO> {

    /**
     * 组装礼包商品查询条件
     *
     * @param query 查询条件
     * @return 条件构造器
     */
    default BaseLambdaQueryWrapper<PackGoodsDO> getLw(PackGoodsQuery query) {
        BaseLambdaQueryWrapper<PackGoodsDO> lw = new BaseLambdaQueryWrapper<>();
        lw.notNullEq(PackGoodsDO::getId, query.getId())
                .notNullEq(PackGoodsDO::getType, query.getType())
                .notNullEq(PackGoodsDO::getLevel, query.getLevel())
                .notNullEq(PackGoodsDO::getState, query.getState())
                .notEmptyLike(PackGoodsDO::getName, query.getName());
        lw.notEmptyIn(PackGoodsDO::getType, query.getTypeList());
        lw.notEmptyIn(PackGoodsDO::getId, query.getIdList());
        lw.orderBy(query);
        return lw;
    }
}
