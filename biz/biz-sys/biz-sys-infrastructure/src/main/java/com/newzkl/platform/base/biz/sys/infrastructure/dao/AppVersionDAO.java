package com.newzkl.platform.base.biz.sys.infrastructure.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.AppVersionDO;
import com.newzkl.platform.base.biz.sys.model.appversion.query.AppVersionQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * app 版本 DAO
 *
 * @author fang
 */
@Mapper
public interface AppVersionDAO extends BaseMapper<AppVersionDO> {

    /**
     * 构建 app 版本查询条件
     *
     * @param query 查询条件
     * @return 查询条件
     */
    default LambdaQueryWrapper<AppVersionDO> getLw(AppVersionQuery query) {
        return new BaseLambdaQueryWrapper<AppVersionDO>()
                .notEmptyEq(AppVersionDO::getAppName, query.getAppName())
                .orderByDesc(AppVersionDO::getId);
    }
}
