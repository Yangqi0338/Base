package com.newzkl.platform.base.biz.account.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.RoleQuery;
import com.newzkl.platform.base.biz.account.model.role.vo.RoleVO;

import java.util.List;

/**
 * 角色仓储端口。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.repository.IRoleRepository} (表 {@code role})。
 * 旧 {@code roleEdit(List<EditColumnDTO>, id)} (按列增量更新) 无调用方 (角色计数走
 * {@link #addCount(Long)}), 未随本切片迁移。</p>
 *
 * @author KC
 */
public interface RoleRepository {

    /**
     * 保存角色。
     *
     * @param role 角色领域视图 (需带 id)
     * @return 主键 ID
     */
    Long save(RoleVO role);

    /**
     * 按主键更新角色 (仅更新非 null 列)。
     *
     * @param role 角色领域视图 (需带 id)
     * @return 影响行数
     */
    int edit(RoleVO role);

    /**
     * 按 ID 列表删除角色。
     *
     * @param idList ID 列表
     * @return 影响行数
     */
    int delete(List<Long> idList);

    /**
     * 角色详情。
     *
     * @param id 角色 ID
     * @return 角色领域视图, 无则 null
     */
    RoleVO detail(Long id);

    /**
     * 角色列表 (不分页)。
     *
     * @param query 查询条件
     * @return 角色列表, 无数据返回空集合
     */
    List<RoleVO> list(RoleQuery query);

    /**
     * 角色分页。
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<RoleVO> pageList(RoleQuery query);

    /**
     * 角色用户量自增 1。
     *
     * <p>保留旧 mapper {@code addCount} 语义: {@code total_user_num = total_user_num + 1}。</p>
     *
     * @param id 角色 ID
     * @return 影响行数
     */
    int addCount(Long id);
}
