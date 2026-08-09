package com.newzkl.platform.base.biz.course.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.model.category.query.CourseCategoryQuery;
import com.newzkl.platform.base.biz.course.model.category.req.CourseCategoryReq;
import com.newzkl.platform.base.biz.course.model.category.res.CourseCategoryRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

import java.util.List;

/**
 * 课程分类仓储端口
 *
 * @author KC
 */
public interface CourseCategoryRepository {

    /**
     * 新增或更新分类
     *
     * @param req 分类请求
     * @return 分类主键
     */
    Long save(CourseCategoryReq req);

    /**
     * 按主键查分类
     *
     * @param id 分类主键
     * @return 分类视图, 不存在返回 null
     */
    CourseCategoryRes detail(Long id);

    /**
     * 分页查分类
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<CourseCategoryRes> pageList(CourseCategoryQuery query);

    /**
     * 查全部启用分类, 按 sort 升序
     *
     * @return 分类列表, 永远非 null
     */
    List<CourseCategoryRes> listAllEnabled();

    /**
     * 更新启用状态
     *
     * @param id        分类主键
     * @param isEnabled 启用状态 1-启用 0-禁用
     * @return 是否更新成功
     */
    boolean updateEnabled(Long id, CommonEnum.YesOrNo isEnabled);

    /**
     * 判断分类名称是否已存在
     *
     * @param categoryName 分类名称
     * @param excludeId    排除的主键, 编辑场景传自身 id, 新增传 null
     * @return 是否存在
     */
    boolean existsByName(String categoryName, Long excludeId);

    /**
     * 逻辑删除分类
     *
     * @param idList 分类主键列表
     * @return 是否删除成功
     */
    boolean delete(List<Long> idList);

    /**
     * 恢复已逻辑删除的分类
     *
     * @param id 分类主键
     * @return 是否恢复成功
     */
    boolean recover(Long id);
}
