package com.newzkl.platform.base.biz.course.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterRes;
import com.newzkl.platform.base.biz.course.model.watch.query.CourseChapterWatchRecordQuery;
import com.newzkl.platform.base.biz.course.model.watch.req.CourseChapterWatchRecordReq;
import com.newzkl.platform.base.biz.course.model.watch.res.CourseChapterWatchRecordRes;

import java.util.List;

/**
 * 课程章节观看记录领域服务
 *
 * @author KC
 */
public interface CourseChapterWatchRecordDomain {

    /**
     * 观看记录 upsert
     *
     * <p>按 (userId, courseChapterId) 判重: 已存在则累加观看次数并刷新观看时间, 否则新增。
     * HTTP 入口的 userId 由 {@code UserAccountApi#currentUserId()} 覆写为当前登录用户;
     * MQ 异步通道由消息体携带 userId, 不覆写。</p>
     *
     * @param req         观看记录请求
     * @return 观看记录视图
     */
    CourseChapterWatchRecordRes saveOrUpdate(CourseChapterWatchRecordReq req);

    /**
     * 缓冲一次章节观看事件
     *
     * <p>章节详情查询时调用, 取当前登录用户 ID 组装观看事件写入 Redis 缓冲队列, 由定时同步任务批量落库
     * (替代旧 MQ 异步刷新)。无登录用户则跳过; 内部异常仅记录日志, 不影响查询主流程</p>
     *
     * @param chapter 章节详情
     */
    void bufferWatch(CourseChapterRes chapter);

    /**
     * 同步观看缓冲队列到数据库
     *
     * <p>由定时任务触发, 逐条 drain 缓冲队列中的观看事件并 upsert 观看记录(存在累加观看次数, 否则新增),
     * 单次上限见 {@code CourseChapterWatchRedisConstant.SYNC_BATCH_SIZE}。逐条落库语义与旧 MQ 消费一致</p>
     */
    void syncBucketToDb();

    /**
     * 按用户与章节查观看记录
     *
     * @param userId          用户ID
     * @param courseChapterId 章节ID
     * @return 观看记录, 不存在返回 null
     */
    CourseChapterWatchRecordRes getByUserIdAndChapterId(Long userId, Long courseChapterId);

    /**
     * 分页查观看记录
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<CourseChapterWatchRecordRes> pageQuery(CourseChapterWatchRecordQuery query);

    /**
     * 查用户在某课程下已观看的章节记录
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 记录列表, 永远非 null
     */
    List<CourseChapterWatchRecordRes> listWatchedByUserIdAndCourseId(Long userId, Long courseId);

    /**
     * 删除观看记录
     *
     * @param id 记录主键
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 批量删除观看记录
     *
     * @param idList 记录主键列表
     * @return 是否成功
     */
    boolean batchDelete(List<Long> idList);
}
