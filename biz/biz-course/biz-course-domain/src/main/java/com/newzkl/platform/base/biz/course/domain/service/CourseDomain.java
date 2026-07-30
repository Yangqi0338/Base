package com.newzkl.platform.base.biz.course.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.model.course.query.CourseQuery;
import com.newzkl.platform.base.biz.course.model.course.req.CourseDetailReq;
import com.newzkl.platform.base.biz.course.model.course.req.CourseReq;
import com.newzkl.platform.base.biz.course.model.course.res.AppCourseRes;
import com.newzkl.platform.base.biz.course.model.course.res.CourseRes;

import java.util.List;

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
     * @param courseNum 课程编码
     * @return 课程视图
     */
    CourseRes getByCourseNum(String courseNum);

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
}
