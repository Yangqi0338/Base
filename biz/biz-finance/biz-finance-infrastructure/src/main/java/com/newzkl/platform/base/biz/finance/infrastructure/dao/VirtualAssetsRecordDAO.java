package com.newzkl.platform.base.biz.finance.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.VirtualAssetsRecordDO;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsRecordQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 虚拟资产变动记录 (virtual_assets_record) 表数据库访问层。
 *
 * <p>纯 MyBatis-Plus 实现, 无自定义 SQL, 故无需 mapper xml。</p>
 *
 * @author KC
 */
@Mapper
public interface VirtualAssetsRecordDAO extends BaseMapper<VirtualAssetsRecordDO> {

    /**
     * 组装查询条件。
     *
     * @param query 查询条件
     * @return lambda 查询包装器
     */
    default BaseLambdaQueryWrapper<VirtualAssetsRecordDO> getLw(VirtualAssetsRecordQuery query) {
        BaseLambdaQueryWrapper<VirtualAssetsRecordDO> ew = new BaseLambdaQueryWrapper<>();
        ew.notNullEq(VirtualAssetsRecordDO::getAccountId, query.getAccountId());
        ew.notNullEq(VirtualAssetsRecordDO::getAccountType, query.getAccountType());
        ew.notNullEq(VirtualAssetsRecordDO::getAssetsType, query.getAssetsType());
        ew.notNullEq(VirtualAssetsRecordDO::getBusinessType, query.getBusinessType());
        ew.notNullEq(VirtualAssetsRecordDO::getAlterType, query.getAlterType());
        return ew;
    }
}
