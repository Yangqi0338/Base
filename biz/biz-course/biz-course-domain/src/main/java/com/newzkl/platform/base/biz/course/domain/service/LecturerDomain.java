package com.newzkl.platform.base.biz.course.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.model.lecturer.query.LecturerQuery;
import com.newzkl.platform.base.biz.course.model.lecturer.req.LecturerReq;
import com.newzkl.platform.base.biz.course.model.lecturer.res.LecturerRes;
import com.newzkl.platform.base.biz.course.model.lecturercategory.query.LecturerCategoryQuery;
import com.newzkl.platform.base.biz.course.model.lecturercategory.req.LecturerCategoryReq;
import com.newzkl.platform.base.biz.course.model.lecturercategory.res.LecturerCategoryRes;

import java.util.List;

/**
 * 讲师领域服务
 *
 * <p>{@code mainAccountId} 为存储型外键(讲师绑定的主体账号), 非运行时跨域调用。</p>
 *
 * @author KC
 */
public interface LecturerDomain {

    /**
     * 新增讲师, 同一主体账号仅允许绑定一个讲师
     *
     * @param req 讲师请求
     * @return 讲师视图
     */
    LecturerRes add(LecturerReq req);

    /**
     * 编辑讲师
     *
     * @param req 讲师请求
     * @return 讲师视图
     */
    LecturerRes edit(LecturerReq req);

    /**
     * 启用讲师
     *
     * @param id 讲师主键
     * @return 是否成功
     */
    boolean enable(Long id);

    /**
     * 禁用讲师
     *
     * @param id 讲师主键
     * @return 是否成功
     */
    boolean disable(Long id);

    /**
     * 按主键查讲师
     *
     * @param id 讲师主键
     * @return 讲师视图
     */
    LecturerRes getById(Long id);

    /**
     * 分页查讲师
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<LecturerRes> pageQuery(LecturerQuery query);

    /**
     * 查全部启用讲师
     *
     * @return 讲师列表, 永远非 null
     */
    List<LecturerRes> listAllEnabled();

    /**
     * 按主体账号ID查讲师
     *
     * @param mainAccountId 主体账号ID
     * @return 讲师视图, 不存在返回 null
     */
    LecturerRes getByMainAccountId(Long mainAccountId);

    /**
     * 删除讲师, 讲师下有课程时拒绝
     *
     * @param id 讲师主键
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 关注数增减, 供关注/取关切片回写
     *
     * @param id    讲师主键
     * @param delta 增量, 取关传负数
     * @return 是否成功
     */
    boolean addFollowCount(Long id, int delta);

    /**
     * 新增讲师分类
     *
     * @param req 分类请求
     * @return 分类视图
     */
    LecturerCategoryRes addCategory(LecturerCategoryReq req);

    /**
     * 编辑讲师分类
     *
     * @param req 分类请求
     * @return 分类视图
     */
    LecturerCategoryRes editCategory(LecturerCategoryReq req);

    /**
     * 启用讲师分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean enableCategory(Long id);

    /**
     * 禁用讲师分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean disableCategory(Long id);

    /**
     * 按主键查讲师分类
     *
     * @param id 分类主键
     * @return 分类视图
     */
    LecturerCategoryRes getCategoryById(Long id);

    /**
     * 分页查讲师分类
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<LecturerCategoryRes> pageCategoryQuery(LecturerCategoryQuery query);

    /**
     * 查全部启用讲师分类
     *
     * @return 平铺分类列表, 永远非 null
     */
    List<LecturerCategoryRes> listAllCategoryEnabled();
}
