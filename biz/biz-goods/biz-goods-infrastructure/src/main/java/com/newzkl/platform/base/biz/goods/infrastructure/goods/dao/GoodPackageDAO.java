package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.GoodPackageDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.goodPackage.GoodPackageQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品-套餐 DAO
 *
 * <p>纯 MyBatis-Plus, 无 mapper xml。</p>
 *
 * @author KC
 */
@Mapper
public interface GoodPackageDAO extends BaseMapper<GoodPackageDO> {

    /**
     * 构建套餐查询条件
     *
     * @param query 套餐查询
     * @return 查询条件
     */
    default LambdaQueryWrapper<GoodPackageDO> getLw(GoodPackageQuery query) {
        return new BaseLambdaQueryWrapper<GoodPackageDO>()
                .notEmptyIn(GoodPackageDO::getId, query.getIdList())
                .notEmptyEq(GoodPackageDO::getPackageId, query.getPackageId())
                .notEmptyLike(GoodPackageDO::getPackageName, query.getPackageName())
                .notNullEq(GoodPackageDO::getState, query.getState())
                .orderByDesc(GoodPackageDO::getCreateTime);
    }

    /**
     * 构建按套餐业务编码的查询条件
     *
     * @param packageId 套餐业务编码
     * @return 查询条件
     */
    default LambdaQueryWrapper<GoodPackageDO> getLwByPackageId(String packageId) {
        return new BaseLambdaQueryWrapper<GoodPackageDO>()
                .notEmptyEq(GoodPackageDO::getPackageId, packageId);
    }
}
