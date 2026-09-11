package com.newzkl.platform.base.biz.course.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.model.category.query.CourseCategoryQuery;
import com.newzkl.platform.base.biz.course.model.category.req.CourseCategoryReq;
import com.newzkl.platform.base.biz.course.model.category.res.CourseCategoryRes;
import com.newzkl.platform.base.biz.course.model.chapter.query.CourseChapterQuery;
import com.newzkl.platform.base.biz.course.model.chapter.req.CourseChapterReq;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterRes;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterStatRes;
import com.newzkl.platform.base.biz.course.model.course.query.CourseQuery;
import com.newzkl.platform.base.biz.course.model.course.req.CourseDetailReq;
import com.newzkl.platform.base.biz.course.model.course.req.CourseReq;
import com.newzkl.platform.base.biz.course.model.course.res.AppCourseRes;
import com.newzkl.platform.base.biz.course.model.course.res.CourseRes;

import java.util.List;
import java.util.Map;

/**
 * 课程领域服务
 *
 * @author KC
 */
public interface CourseDomain {

    /**
     * 新增课程, 自动生成 K 前缀课程编码
     *
     * @param req 课程请求
     * @return 课程视图
     */
    CourseRes add(CourseReq req);

    /**
     * 编辑课程基础信息, 课程编码不可改
     *
     * @param req 课程请求
     * @return 课程视图
     */
    CourseRes editBase(CourseReq req);

    /**
     * 编辑课程详情(封面/轮播/视频/富文本)
     *
     * @param req 课程详情请求
     * @return 课程视图
     */
    CourseRes editDetail(CourseDetailReq req);

    /**
     * 上架课程
     *
     * @param id 课程主键
     * @return 是否成功
     */
    boolean enable(Long id);

    /**
     * 下架课程
     *
     * @param id 课程主键
     * @return 是否成功
     */
    boolean disable(Long id);

    /**
     * 按主键查课程
     *
     * @param id 课程主键
     * @return 课程视图
     */
    CourseRes getById(Long id);

    /**
     * 按课程编码查课程
     *
     * @param courseNo 课程编码
     * @return 课程视图
     */
    CourseRes getByCourseNo(String courseNo);

    /**
     * 分页查课程(管理端)
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<CourseRes> pageQuery(CourseQuery query);

    /**
     * 分页查课程(C 端), 回填当前用户已观看章节数
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<AppCourseRes> appPageQuery(CourseQuery query);

    /**
     * 按分类查课程列表
     *
     * @param categoryId 课程分类主键
     * @return 课程列表, 永远非 null
     */
    List<CourseRes> listByCategoryId(Long categoryId);

    /**
     * 删除课程
     *
     * @param id 课程主键
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 批量删除课程
     *
     * @param idList 课程主键列表
     * @return 是否成功
     */
    boolean batchDelete(List<Long> idList);

    /**
     * 恢复已删除课程
     *
     * @param id 课程主键
     * @return 是否成功
     */
    boolean recover(Long id);

    /**
     * 实际购买数递增
     *
     * <p>购买编排在 biz-benefit-order, 经 {@code CourseFacade#addPurchaseCount} 回调本方法。</p>
     *
     * @param courseId 课程主键
     * @return 是否成功
     */
    boolean addPurchaseCount(Long courseId);

    /**
     * 刷新课程章节统计冗余字段
     *
     * @param courseId 课程主键
     * @return 是否成功
     */
    boolean refreshChapterStat(Long courseId);

    /**
     * 新增分类, 自动生成 KF 前缀编码
     *
     * @param req 分类请求
     * @return 分类视图
     */
    CourseCategoryRes addCategory(CourseCategoryReq req);

    /**
     * 编辑分类, 分类编码不可改
     *
     * @param req 分类请求
     * @return 分类视图
     */
    CourseCategoryRes editCategory(CourseCategoryReq req);

    /**
     * 启用分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean enableCategory(Long id);

    /**
     * 禁用分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean disableCategory(Long id);

    /**
     * 按主键查分类
     *
     * @param id 分类主键
     * @return 分类视图
     */
    CourseCategoryRes getCategoryById(Long id);

    /**
     * 分页查分类
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<CourseCategoryRes> pageCategoryQuery(CourseCategoryQuery query);

    /**
     * 查全部启用分类
     *
     * @return 平铺分类列表, 永远非 null
     */
    List<CourseCategoryRes> listAllCategoryEnabled();

    /**
     * 删除分类, 分类下有课程时拒绝
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean deleteCategory(Long id);

    /**
     * 批量删除分类
     *
     * @param idList 分类主键列表
     * @return 是否成功
     */
    boolean batchDeleteCategory(List<Long> idList);

    /**
     * 恢复已删除分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    boolean recoverCategory(Long id);

    /**
     * 新增章节, 同一课程下章节数不可重复
     *
     * @param req 章节请求
     * @return 章节视图
     */
    CourseChapterRes addChapter(CourseChapterReq req);

    /**
     * 编辑章节
     *
     * @param req 章节请求
     * @return 章节视图
     */
    CourseChapterRes editChapter(CourseChapterReq req);

    /**
     * 启用章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    boolean enableChapter(Long id);

    /**
     * 禁用章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    boolean disableChapter(Long id);

    /**
     * 设为免费
     *
     * @param id 章节主键
     * @return 是否成功
     */
    boolean setFree(Long id);

    /**
     * 设为收费
     *
     * @param id 章节主键
     * @return 是否成功
     */
    boolean setCharge(Long id);

    /**
     * 按主键查章节
     *
     * @param id 章节主键
     * @return 章节视图
     */
    CourseChapterRes getChapterById(Long id);

    /**
     * 分页查章节
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<CourseChapterRes> pageChapterQuery(CourseChapterQuery query);

    /**
     * 按课程编码查章节列表
     *
     * @param courseNo 课程编码
     * @return 章节列表, 永远非 null
     */
    List<CourseChapterRes> listChapterByCourseNo(String courseNo);

    /**
     * 批量统计课程章节数与总时长
     *
     * <p>供 biz-benefit-order 的"我的已购课程"列表消费, 经 {@code CourseFacade#batchStatChapter} 暴露。</p>
     *
     * @param courseIdList 课程主键列表
     * @return 课程主键到统计的映射, 永远非 null
     */
    Map<Long, CourseChapterStatRes> batchStatChapter(List<Long> courseIdList);

    /**
     * 删除章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    boolean deleteChapter(Long id);

    /**
     * 批量删除章节
     *
     * @param idList 章节主键列表
     * @return 是否成功
     */
    boolean batchDeleteChapter(List<Long> idList);

    /**
     * 恢复已删除章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    boolean recoverChapter(Long id);
}
