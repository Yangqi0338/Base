package com.newzkl.platform.base.biz.course.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.model.category.query.CourseCategoryQuery;
import com.newzkl.platform.base.biz.course.model.category.req.CourseCategoryReq;
import com.newzkl.platform.base.biz.course.model.category.res.CourseCategoryRes;

import java.util.List;

/**
 * 课程分类领域服务
 *
 * <p>分类为扁平单层结构, 无父子层级, {@link CourseCategoryDomain#listAllEnabled} 返回平铺列表。</p>
 *
 * @author KC
 */
public interface CourseCategoryDomain {

    /**
     * 新增分类, 自动生成 KF 前缀编码
     *
     * @param req 分类请求
     * @return 分类视图
     */
    CourseCategoryRes add(CourseCategoryReq req);

    /**
     * 编辑分类, 分类编码不可改
     *
     * @param req 分类请求
     * @return 分类视图
     */
    CourseCategoryRes edit(CourseCategoryReq req);

    /**
     * 启用分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean enable(Long id);

    /**
     * 禁用分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean disable(Long id);

    /**
     * 按主键查分类
     *
     * @param id 分类主键
     * @return 分类视图
     */
    CourseCategoryRes getById(Long id);

    /**
     * 分页查分类
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<CourseCategoryRes> pageQuery(CourseCategoryQuery query);

    /**
     * 查全部启用分类
     *
     * @return 平铺分类列表, 永远非 null
     */
    List<CourseCategoryRes> listAllEnabled();

    /**
     * 删除分类, 分类下有课程时拒绝
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 批量删除分类
     *
     * @param idList 分类主键列表
     * @return 是否成功
     */
    boolean batchDelete(List<Long> idList);

    /**
     * 恢复已删除分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean recover(Long id);
}
