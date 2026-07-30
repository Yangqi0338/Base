package com.newzkl.platform.base.biz.course.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.model.lecturercategory.query.LecturerCategoryQuery;
import com.newzkl.platform.base.biz.course.model.lecturercategory.req.LecturerCategoryReq;
import com.newzkl.platform.base.biz.course.model.lecturercategory.res.LecturerCategoryRes;

import java.util.List;

/**
 * 讲师分类领域服务
 *
 * <p>分类为扁平单层结构, 无父子层级。源无删除/恢复端点, 故不提供删除能力。</p>
 *
 * @author KC
 */
public interface LecturerCategoryDomain {

    /**
     * 新增讲师分类
     *
     * @param req 分类请求
     * @return 分类视图
     */
    LecturerCategoryRes add(LecturerCategoryReq req);

    /**
     * 编辑讲师分类
     *
     * @param req 分类请求
     * @return 分类视图
     */
    LecturerCategoryRes edit(LecturerCategoryReq req);

    /**
     * 启用讲师分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean enable(Long id);

    /**
     * 禁用讲师分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean disable(Long id);

    /**
     * 按主键查讲师分类
     *
     * @param id 分类主键
     * @return 分类视图
     */
    LecturerCategoryRes getById(Long id);

    /**
     * 分页查讲师分类
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<LecturerCategoryRes> pageQuery(LecturerCategoryQuery query);

    /**
     * 查全部启用讲师分类
     *
     * @return 平铺分类列表, 永远非 null
     */
    List<LecturerCategoryRes> listAllEnabled();
}
