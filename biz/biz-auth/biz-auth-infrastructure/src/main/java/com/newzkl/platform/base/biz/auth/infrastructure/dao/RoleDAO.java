package com.newzkl.platform.base.biz.auth.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.auth.infrastructure.dao.po.RoleDO;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色 DAO
 *
 * <p>面向 {@code role} 表 (可申请的企业角色配置), 与鉴权角色 {@code auth_role} 的 DAO 无关。</p>
 *
 * @author KC
 */
@Mapper
public interface RoleDAO extends BaseMapper<RoleDO> {

    /**
     * 构建角色查询条件
     *
     * <p>对齐旧 mapper {@code RoleDAO.xml} 的 where 片段: id / idList 精确匹配,
     * {@code name} 模糊匹配; 排序沿用旧 {@code listByQuery} 的按创建时间倒序。</p>
     *
     * @param query 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<RoleDO> getLw(RoleQuery query) {
        BaseLambdaQueryWrapper<RoleDO> wrapper = new BaseLambdaQueryWrapper<RoleDO>()
                .notEmptyIn(RoleDO::getId, query.getIdList())
                .notEmptyLike(RoleDO::getName, query.getName());
        wrapper.orderByDesc(RoleDO::getCreateTime);
        return wrapper;
    }
}
