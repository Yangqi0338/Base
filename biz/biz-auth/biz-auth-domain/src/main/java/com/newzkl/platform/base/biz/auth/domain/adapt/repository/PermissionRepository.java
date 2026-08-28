package com.newzkl.platform.base.biz.auth.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.model.permission.req.PermissionQuery;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionDTO;

import java.util.Collection;
import java.util.List;

/**
 * 权限仓储
 *
 * @author KC
 */
public interface PermissionRepository {

    /**
     * 新增权限
     *
     * @param dto 权限数据
     * @return 主键ID
     */
    Long insert(PermissionDTO dto);

    /**
     * 更新权限
     *
     * @param dto 权限数据
     */
    void update(PermissionDTO dto);

    /**
     * 按ID集合逻辑删除
     *
     * @param ids 主键集合
     */
    void softDeleteByIds(Collection<Long> ids);

    /**
     * 按ID查询
     *
     * @param id 主键
     * @return 权限数据, 未命中返回 null
     */
    PermissionDTO getById(Long id);

    /**
     * 按端与编码查询
     *
     * @param client 所属端
     * @param code   权限编码
     * @return 权限数据, 未命中返回 null
     */
    PermissionDTO getByCode(AccountEnum.Client client, String code);

    /**
     * 按端与类型列出
     *
     * @param client 所属端
     * @param type   权限类型
     * @return 权限列表
     */
    List<PermissionDTO> listByType(AccountEnum.Client client, PermissionEnum.Type type);

    /**
     * 按ID集合列出
     *
     * @param ids 主键集合
     * @return 权限列表
     */
    List<PermissionDTO> listByIds(Collection<Long> ids);

    /**
     * 按编码集合列出
     *
     * @param codes 编码集合
     * @return 权限列表
     */
    List<PermissionDTO> listByCodes(Collection<String> codes);

    Page<PermissionDTO> page(PermissionQuery query);
}
