package com.newzkl.platform.base.biz.auth.domain.service;

import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionDTO;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionListDTO;
import com.newzkl.platform.base.biz.auth.model.permission.req.PermissionReq;
import com.newzkl.platform.base.biz.auth.model.permission.vo.PermissionTreeVO;
import com.newzkl.platform.base.biz.auth.model.permission.vo.PermissionVO;

import java.util.List;

/**
 * 权限领域服务
 *
 * @author KC
 */
public interface PermissionDomain {

    /**
     * 创建菜单权限
     *
     * @param req 权限入参
     * @return 主键ID
     */
    Long create(PermissionReq req);

    /**
     * 更新权限
     *
     * @param req 权限入参
     */
    void update(PermissionReq req);

    /**
     * 逻辑删除菜单权限
     *
     * @param id 主键
     */
    void delete(Long id);

    /**
     * 权限详情
     *
     * @param id 主键
     * @return 权限视图
     */
    PermissionVO detail(Long id);

    /**
     * 按类型构建权限树
     *
     * @param type 权限类型
     * @return 权限树
     */
    List<PermissionTreeVO> tree(PermissionEnum.Type type);

    /**
     * 批量导入菜单
     *
     * @param menus 菜单树
     * @return 处理节点数
     */
    int importMenu(PermissionListDTO menus);

    /**
     * 按权限反查并重算受影响账号
     *
     * @param permissionId 权限ID
     * @return 受影响账号数
     */
    int syncAccountsByPermission(Long permissionId);

    /**
     * 同步功能权限节点
     *
     * <p>入参为扫描层构建的类节点树, 每个类节点的 {@code children} 为其方法节点,
     * 领域层负责按 code upsert(类节点先落库拿ID, 方法节点回填 pid), 未命中的存量功能节点软删,
     * 并重算受影响账号权限</p>
     *
     * @param classNodes 类节点树, 每个含方法子节点
     * @return 同步结果统计
     */
    SyncResult syncFunc(List<PermissionDTO> classNodes);

    /**
     * 功能同步结果
     *
     * @param created     新增节点数
     * @param updated     更新节点数
     * @param softDeleted 软删孤儿节点数
     */
    record SyncResult(int created, int updated, int softDeleted) {
    }
}
