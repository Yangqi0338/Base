package com.newzkl.platform.base.biz.auth.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.auth.infrastructure.entity.PermissionDO;
import com.newzkl.platform.base.biz.auth.model.permission.req.PermissionQuery;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

/**
 * 权限 DAO
 *
 * @author KC
 */
@Mapper
public interface PermissionDAO extends BaseMapper<PermissionDO> {

    /**
     * 按端与类型构建权限查询条件, 按排序升序
     *
     * @param client 所属端, 为空则不限端
     * @param type   权限类型
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<PermissionDO> getLw(AccountEnum.Client client, PermissionEnum.Type type) {
        BaseLambdaQueryWrapper<PermissionDO> wrapper = new BaseLambdaQueryWrapper<PermissionDO>()
                .notNullEq(PermissionDO::getClient, client)
                .notNullEq(PermissionDO::getType, type);
        wrapper.orderByAsc(PermissionDO::getSort);
        return wrapper;
    }

    default BaseLambdaQueryWrapper<PermissionDO> getLw(PermissionQuery query) {
        BaseLambdaQueryWrapper<PermissionDO> wrapper = new BaseLambdaQueryWrapper<PermissionDO>()
                .notNullEq(PermissionDO::getClient, query.getClient())
                .notNullEq(PermissionDO::getType, query.getType())
                .notEmptyIn(PermissionDO::getId, query.getIdList())
                ;
        wrapper.orderByAsc(PermissionDO::getSort);
        return wrapper;
    }

    /**
     * 按编码集合构建权限查询条件
     *
     * @param codes 权限编码集合
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<PermissionDO> getLwByCodes(Collection<String> codes) {
        return new BaseLambdaQueryWrapper<PermissionDO>()
                .notEmptyIn(PermissionDO::getCode, codes);
    }
}
