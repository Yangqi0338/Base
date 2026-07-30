package com.newzkl.platform.base.biz.course.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.model.chapter.query.CourseChapterQuery;
import com.newzkl.platform.base.biz.course.model.chapter.req.CourseChapterReq;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterRes;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterStatRes;

import java.util.List;
import java.util.Map;

/**
 * 课程章节领域服务
 *
 * @author KC
 */
public interface CourseChapterDomain {

    /**
     * 新增章节, 同一课程下章节数不可重复
     *
     * @param req 章节请求
     * @return 章节视图
     */
    CourseChapterRes add(CourseChapterReq req);

    /**
     * 编辑章节
     *
     * @param req 章节请求
     * @return 章节视图
     */
    CourseChapterRes edit(CourseChapterReq req);

    /**
     * 启用章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    boolean enable(Long id);

    /**
     * 禁用章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    boolean disable(Long id);

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
    CourseChapterRes getById(Long id);

    /**
     * 分页查章节
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<CourseChapterRes> pageQuery(CourseChapterQuery query);

    /**
     * 按课程编码查章节列表
     *
     * @param courseNum 课程编码
     * @return 章节列表, 永远非 null
     */
    List<CourseChapterRes> listByCourseNum(String courseNum);

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
    boolean delete(Long id);

    /**
     * 批量删除章节
     *
     * @param idList 章节主键列表
     * @return 是否成功
     */
    boolean batchDelete(List<Long> idList);

    /**
     * 恢复已删除章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    boolean recover(Long id);
}
