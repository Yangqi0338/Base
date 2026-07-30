package com.newzkl.platform.base.biz.course.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.model.lecturercategory.query.LecturerCategoryQuery;
import com.newzkl.platform.base.biz.course.model.lecturercategory.req.LecturerCategoryReq;
import com.newzkl.platform.base.biz.course.model.lecturercategory.res.LecturerCategoryRes;

import java.util.List;

/**
 * 讲师分类仓储端口
 *
 * @author KC
 */
public interface LecturerCategoryRepository {

    /**
     * 新增或更新讲师分类
     *
     * @param req 分类请求
     * @return 分类主键
     */
    Long save(LecturerCategoryReq req);

    /**
     * 按主键查讲师分类
     *
     * @param id 分类主键
     * @return 分类视图, 不存在返回 null
     */
    LecturerCategoryRes detail(Long id);

    /**
     * 分页查讲师分类
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<LecturerCategoryRes> pageList(LecturerCategoryQuery query);

    /**
     * 查全部启用讲师分类
     *
     * @return 分类列表, 永远非 null
     */
    List<LecturerCategoryRes> listAllEnabled();

    /**
     * 更新启用状态
     *
     * @param id        分类主键
     * @param isEnabled 启用状态 1-启用 0-禁用
     * @return 是否更新成功
     */
    boolean updateEnabled(Long id, Integer isEnabled);

    /**
     * 判断分类名称是否已存在
     *
     * @param categoryName 分类名称
     * @param excludeId    排除的主键, 编辑场景传自身 id, 新增传 null
     * @return 是否存在
     */
    boolean existsByName(String categoryName, Long excludeId);
}
