package com.newzkl.platform.base.biz.auth.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.model.permission.dto.RoleDTO;
import com.newzkl.platform.base.biz.auth.model.permission.req.RoleQuery;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;


import java.util.Collection;
import java.util.List;

/**
 * 角色仓储
 *
 * @author KC
 */
public interface RoleRepository {

    /**
     * 新增角色
     *
     * @param dto 角色数据
     * @return 主键ID
     */
    Long insert(RoleDTO dto);

    /**
     * 更新角色
     *
     * @param dto 角色数据
     */
    void update(RoleDTO dto);

    /**
     * 按ID逻辑删除
     *
     * @param id 主键
     */
    void deleteById(Long id);

    /**
     * 按ID查询
     *
     * @param id 主键
     * @return 角色数据, 未命中返回 null
     */
    RoleDTO getById(Long id);

    /**
     * 按端与编码查询
     *
     * @param client 所属端
     * @param code   角色编码
     * @return 角色数据, 未命中返回 null
     */
    RoleDTO getByCode(AccountEnum.Client client, String code);

    /**
     * 列出某端全部角色
     *
     * @param client 所属端
     * @return 角色列表
     */
    List<RoleDTO> listAll(AccountEnum.Client client);

    /**
     * 按ID集合列出
     *
     * @param ids 主键集合
     * @return 角色列表
     */
    List<RoleDTO> listByIds(Collection<Long> ids);

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<RoleDTO> page(RoleQuery query);
}
