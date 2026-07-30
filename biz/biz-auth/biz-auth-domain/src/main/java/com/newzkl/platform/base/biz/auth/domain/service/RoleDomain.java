package com.newzkl.platform.base.biz.auth.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleReq;
import com.newzkl.platform.base.biz.auth.model.role.res.RoleRes;
import com.newzkl.platform.base.biz.auth.model.role.vo.RoleVO;

import java.util.List;

/**
 * 角色领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.service.IRoleDomain} (表 {@code role})。
 * 语义为"可申请的企业角色配置", 与鉴权角色 ({@code auth_role}) 无关。
 * 旧 {@code roleEdit(List<EditColumnDTO>, id)} 无调用方, 未随本切片迁移;
 * 旧 {@code addCount(long)} 参数名虽为 count, 实为角色 ID (mapper 按 id 自增计数),
 * 本仓改名为 {@link RoleDomain#addCount} 以消除误导。</p>
 *
 * @author KC
 */
public interface RoleDomain {

    /**
     * 角色保存
     *
     * <p>主键由领域层雪花生成, 与旧实现一致。</p>
     *
     * @param req 角色入参
     * @return 主键 ID
     */
    Long save(RoleReq req);

    /**
     * 角色修改
     *
     * <p>以入参 {@code id} 为更新目标 (非请求体内的 id), 与旧实现一致。</p>
     *
     * @param id  角色 ID
     * @param req 角色入参
     * @return 影响行数
     */
    int edit(Long id, RoleReq req);

    /**
     * 角色删除
     *
     * @param idList ID 列表
     * @return 影响行数
     */
    int delete(List<Long> idList);

    /**
     * 角色领域视图
     *
     * @param id 角色 ID
     * @return 角色领域视图, 无则 null
     */
    RoleVO role(Long id);

    /**
     * 角色详情 (对外出参)
     *
     * @param id 角色 ID
     * @return 角色出参, 无则 null
     */
    RoleRes detail(Long id);

    /**
     * 角色列表
     *
     * <p>旧 {@code /user/role/roleList} 返回的是分页查询后的列表 (非分页包装),
     * 本方法保留该出参形态。</p>
     *
     * @param query 查询条件
     * @return 角色出参列表, 无数据返回空集合
     */
    List<RoleRes> list(RoleQuery query);

    /**
     * 角色分页
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<RoleRes> pageList(RoleQuery query);

    /**
     * 角色用户量自增 1
     *
     * @param id 角色 ID
     * @return 影响行数
     */
    int addCount(Long id);
}
