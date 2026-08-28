package com.newzkl.platform.base.biz.content.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.model.project.query.ProjectQuery;
import com.newzkl.platform.base.biz.content.model.project.req.ProjectSaveReq;
import com.newzkl.platform.base.biz.content.model.project.res.ProjectListRes;
import com.newzkl.platform.base.biz.content.model.project.res.ProjectRes;

import java.util.List;

/**
 * 项目领域服务
 *
 * @author KC
 */
public interface ProjectDomain {

    /**
     * 项目详情
     *
     * @param id 项目主键
     * @return 项目详情
     */
    ProjectRes detail(Long id);

    /**
     * 项目列表 (不分页)
     *
     * @param query 查询条件
     * @return 项目列表, 永不为 null
     */
    List<ProjectListRes> queryList(ProjectQuery query);

    /**
     * 项目分页
     *
     * <p>出参分页对象, 前端读 {@code records}/{@code total}/{@code current}/{@code size}</p>
     *
     * @param query 查询条件
     * @return 项目分页, 永不为 null
     */
    Page<ProjectListRes> queryPage(ProjectQuery query);

    /**
     * 新增项目
     *
     * @param req 保存请求
     * @return 新增项目主键
     */
    Long add(ProjectSaveReq req);

    /**
     * 编辑项目
     *
     * @param req 保存请求, id 必填
     */
    void edit(ProjectSaveReq req);

    /**
     * 删除项目
     *
     * @param id 项目主键
     */
    void del(Long id);

    /**
     * 标记/取消意向
     *
     * @param id       项目主键
     * @param interest 0 不感兴趣 1 感兴趣
     */
    void interest(Long id, Integer interest);
}
