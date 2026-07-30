package com.newzkl.platform.base.biz.finance.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.VirtualAssetsDO;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 虚拟资产 (virtual_assets) 表数据库访问层
 *
 * <p>纯 MyBatis-Plus 实现, 无自定义 SQL, 故无需 mapper xml。</p>
 *
 * @author KC
 */
@Mapper
public interface VirtualAssetsDAO extends BaseMapper<VirtualAssetsDO> {

    /**
     * 组装查询条件
     *
     * @param query 查询条件
     * @return lambda 查询包装器
     */
    default BaseLambdaQueryWrapper<VirtualAssetsDO> getLw(VirtualAssetsQuery query) {
        BaseLambdaQueryWrapper<VirtualAssetsDO> ew = new BaseLambdaQueryWrapper<>();
        ew.notNullEq(VirtualAssetsDO::getAccountId, query.getAccountId());
        ew.notNullEq(VirtualAssetsDO::getAccountType, query.getAccountType());
        ew.notNullEq(VirtualAssetsDO::getAssetsType, query.getAssetsType());
        return ew;
    }
}
