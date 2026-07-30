package com.newzkl.platform.base.biz.sys.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.model.project.query.ProjectQuery;
import com.newzkl.platform.base.biz.sys.model.project.req.ProjectSaveReq;
import com.newzkl.platform.base.biz.sys.model.project.res.ProjectListRes;
import com.newzkl.platform.base.biz.sys.model.project.res.ProjectRes;

import java.util.List;

/**
 * 项目仓储端口
 *
 * @author KC
 */
public interface ProjectRepository {

    /**
     * 项目详情 (含视频列表与当前账号意向态)
     *
     * @param accountId 当前登录账号 id, 为空时意向态返回 false
     * @param id        项目主键
     * @return 项目详情, 不存在时返回 null
     */
    ProjectRes detail(Long accountId, Long id);

    /**
     * 项目列表 (不分页)
     *
     * @param query 查询条件, accountId 决定意向态
     * @return 项目列表, 永不为 null
     */
    List<ProjectListRes> queryList(ProjectQuery query);

    /**
     * 项目分页
     *
     * <p>返回分页对象而非裸 {@code List}: 实现内已执行 {@code selectPage} 拿到 {@code total},
     * 降级成列表等于 count 查询白跑。见 {@code rules/Architecture.md}
     * 「{@code PageInfo}→{@code IPage/Page} 直返」</p>
     *
     * @param query 查询条件, accountId 决定意向态
     * @return 项目分页, 永不为 null
     */
    Page<ProjectListRes> queryPage(ProjectQuery query);

    /**
     * 新增项目 (含视频子表)
     *
     * @param req 保存请求
     * @return 新增项目主键
     */
    Long insert(ProjectSaveReq req);

    /**
     * 编辑项目 (含视频子表全量覆盖)
     *
     * @param req 保存请求, id 必填
     */
    void edit(ProjectSaveReq req);

    /**
     * 删除项目 (含视频子表)
     *
     * @param id 项目主键
     */
    void del(Long id);

    /**
     * 标记/取消意向
     *
     * @param accountId 当前登录账号 id
     * @param id        项目主键
     * @param interest  0 不感兴趣 1 感兴趣
     */
    void interest(Long accountId, Long id, Integer interest);
}
