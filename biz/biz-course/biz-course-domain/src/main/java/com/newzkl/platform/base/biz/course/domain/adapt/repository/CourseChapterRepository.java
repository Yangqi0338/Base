package com.newzkl.platform.base.biz.course.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.model.chapter.query.CourseChapterQuery;
import com.newzkl.platform.base.biz.course.model.chapter.req.CourseChapterReq;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterRes;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterStatRes;

import java.util.List;

/**
 * 课程章节仓储端口
 *
 * @author KC
 */
public interface CourseChapterRepository {

    /**
     * 新增或更新章节
     *
     * @param req 章节请求, 时长单位秒, 由实现换算为百分秒落库
     * @return 章节主键
     */
    Long save(CourseChapterReq req);

    /**
     * 按主键查章节
     *
     * @param id 章节主键
     * @return 章节视图, 不存在返回 null
     */
    CourseChapterRes detail(Long id);

    /**
     * 分页查章节
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<CourseChapterRes> pageList(CourseChapterQuery query);

    /**
     * 按课程主键查章节列表, 按章节数升序
     *
     * @param courseId 课程主键
     * @return 章节列表, 永远非 null
     */
    List<CourseChapterRes> listByCourseId(Long courseId);

    /**
     * 更新启用状态
     *
     * @param id        章节主键
     * @param isEnabled 启用状态 1-启用 0-禁用
     * @return 是否更新成功
     */
    boolean updateEnabled(Long id, Integer isEnabled);

    /**
     * 更新免费/收费状态
     *
     * @param id     章节主键
     * @param isFree 是否免费 1-是 0-否
     * @return 是否更新成功
     */
    boolean updateFree(Long id, Integer isFree);

    /**
     * 判断同一课程下章节数是否重复
     *
     * @param courseId   课程主键
     * @param chapterNum 章节数
     * @param excludeId  排除的主键, 编辑场景传自身 id, 新增传 null
     * @return 是否重复
     */
    boolean existsChapterNum(Long courseId, Integer chapterNum, Long excludeId);

    /**
     * 批量统计课程章节数与总时长
     *
     * @param courseIdList 课程主键列表
     * @return 统计列表, 永远非 null
     */
    List<CourseChapterStatRes> batchStat(List<Long> courseIdList);

    /**
     * 逻辑删除章节
     *
     * @param idList 章节主键列表
     * @return 是否删除成功
     */
    boolean delete(List<Long> idList);

    /**
     * 恢复已逻辑删除的章节
     *
     * @param id 章节主键
     * @return 是否恢复成功
     */
    boolean recover(Long id);
}
