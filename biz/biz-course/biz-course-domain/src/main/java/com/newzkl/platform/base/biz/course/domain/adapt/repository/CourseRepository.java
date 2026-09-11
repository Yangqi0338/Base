package com.newzkl.platform.base.biz.course.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.model.course.query.CourseQuery;
import com.newzkl.platform.base.biz.course.model.course.req.CourseDetailReq;
import com.newzkl.platform.base.biz.course.model.course.req.CourseReq;
import com.newzkl.platform.base.biz.course.model.course.res.CourseRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

import java.util.List;

/**
 * 课程仓储端口
 *
 * @author KC
 */
public interface CourseRepository {

    /**
     * 新增或更新课程基础信息
     *
     * @param req 课程请求, 价格单位元, 由实现换算为分落库
     * @return 课程主键
     */
    Long saveBase(CourseReq req);

    /**
     * 更新课程详情(封面/轮播/视频/富文本)
     *
     * @param req 课程详情请求
     * @return 是否更新成功
     */
    boolean saveDetail(CourseDetailReq req);

    /**
     * 按主键查课程
     *
     * @param id 课程主键
     * @return 课程视图, 不存在返回 null
     */
    CourseRes detail(Long id);

    /**
     * 按课程编码查课程
     *
     * @param courseNo 课程编码
     * @return 课程视图, 不存在返回 null
     */
    CourseRes getByCourseNo(String courseNo);

    /**
     * 分页查课程
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<CourseRes> pageList(CourseQuery query);

    /**
     * 按分类查课程列表
     *
     * @param categoryId 课程分类主键
     * @return 课程列表, 永远非 null
     */
    List<CourseRes> listByCategoryId(Long categoryId);

    /**
     * 更新启用状态(上下架)
     *
     * @param id        课程主键
     * @param isEnabled 启用状态 1-启用 0-禁用
     * @return 是否更新成功
     */
    boolean updateEnabled(Long id, CommonEnum.YesOrNo isEnabled);

    /**
     * 实际购买数递增
     *
     * @param id 课程主键
     * @return 是否更新成功
     */
    boolean addPurchaseCount(Long id);

    /**
     * 回填章节统计冗余字段
     *
     * <p>总时长描述由实现按百分秒换算填充, 调用方无需传入。</p>
     *
     * @param id                       课程主键
     * @param chapterCount             章节总数
     * @param totalDurationCentisecond 总时长, 单位百分秒
     * @return 是否更新成功
     */
    boolean updateChapterStat(Long id, Integer chapterCount, Long totalDurationCentisecond);

    /**
     * 统计分类下课程数
     *
     * @param categoryId 课程分类主键
     * @return 课程数
     */
    long countByCategoryId(Long categoryId);

    /**
     * 统计讲师下课程数
     *
     * @param lecturerId 讲师主键
     * @return 课程数
     */
    long countByLecturerId(Long lecturerId);

    /**
     * 逻辑删除课程
     *
     * @param idList 课程主键列表
     * @return 是否删除成功
     */
    boolean delete(List<Long> idList);

    /**
     * 恢复已逻辑删除的课程
     *
     * @param id 课程主键
     * @return 是否恢复成功
     */
    boolean recover(Long id);

    /**
     * 按标题模糊 + 分类精确解析课程ID集合
     *
     * <p>供已购课程列表按课程属性收窄使用; 两条件皆空返回空集合(调用方据此判定不收窄)。</p>
     *
     * @param title      课程标题, 模糊; 可空
     * @param categoryId 课程分类ID, 精确; 可空
     * @return 命中课程ID集合, 永远非 null
     */
    List<Long> listIdsByTitleAndCategory(String title, Long categoryId);
}
