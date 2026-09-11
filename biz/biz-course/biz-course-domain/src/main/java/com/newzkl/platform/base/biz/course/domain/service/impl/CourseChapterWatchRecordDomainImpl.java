package com.newzkl.platform.base.biz.course.domain.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseChapterRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseChapterWatchRecordRepository;
import com.newzkl.platform.base.biz.course.domain.service.CourseChapterWatchRecordDomain;
import com.newzkl.platform.base.biz.course.model.chapter.constant.CourseChapterWatchRedisConstant;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterRes;
import com.newzkl.platform.base.biz.course.model.watch.query.CourseChapterWatchRecordQuery;
import com.newzkl.platform.base.biz.course.model.watch.req.CourseChapterWatchRecordReq;
import com.newzkl.platform.base.biz.course.model.watch.res.CourseChapterWatchRecordRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.core.redis.utils.RedisListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 课程章节观看记录领域服务实现
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.service.impl.CourseChapterWatchRecordDomainServiceImpl}
 * 与应用服务 {@code CourseChapterWatchRecordServiceImpl} 的合并结果。</p>
 *
 * <p>{@code courseNo}/{@code chapterNum} 为冗余字段, 入参未带时由章节回读补齐, 与旧实现一致。</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseChapterWatchRecordDomainImpl implements CourseChapterWatchRecordDomain {

    private final CourseChapterWatchRecordRepository courseChapterWatchRecordRepository;

    private final CourseChapterRepository courseChapterRepository;

    private final AccountApi userAccountApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseChapterWatchRecordRes saveOrUpdate(CourseChapterWatchRecordReq req) {

            Long currentUserId = userAccountApi.currentUserId();
            ThrowsException.isNull(currentUserId, BaseErrorCode.USER_NOT_LOGIN, "观看记录");
            req.setUserId(currentUserId);

        ThrowsException.isNull(req.getUserId(), BaseErrorCode.PARAM, "用户ID");
        ThrowsException.isFalse(userAccountApi.existsUser(req.getUserId()),
                BaseErrorCode.USER_NOT_FOUND, String.valueOf(req.getUserId()));

        CourseChapterRes chapter = courseChapterRepository.detail(req.getCourseChapterId());
        ThrowsException.isNull(chapter, BaseErrorCode.NODATA, "课程章节");
        if (req.getCourseId() == null) {
            req.setCourseId(chapter.getCourseId());
        }
        if (req.getChapterNum() == null) {
            req.setChapterNum(chapter.getChapterNum());
        }

        CourseChapterWatchRecordRes exist = courseChapterWatchRecordRepository
                .getByUserIdAndChapterId(req.getUserId(), req.getCourseChapterId());
        if (exist != null) {
            courseChapterWatchRecordRepository.addWatchTimes(exist.getId());
            return courseChapterWatchRecordRepository
                    .getByUserIdAndChapterId(req.getUserId(), req.getCourseChapterId());
        }
        courseChapterWatchRecordRepository.save(req);
        return courseChapterWatchRecordRepository
                .getByUserIdAndChapterId(req.getUserId(), req.getCourseChapterId());
    }

    @Override
    public CourseChapterWatchRecordRes getByUserIdAndChapterId(Long userId, Long courseChapterId) {
        ThrowsException.isNull(userId, BaseErrorCode.PARAM, "用户ID");
        ThrowsException.isNull(courseChapterId, BaseErrorCode.PARAM, "章节ID");
        return courseChapterWatchRecordRepository.getByUserIdAndChapterId(userId, courseChapterId);
    }

    @Override
    public IPage<CourseChapterWatchRecordRes> pageQuery(CourseChapterWatchRecordQuery query) {
        return courseChapterWatchRecordRepository.pageList(query);
    }

    @Override
    public List<CourseChapterWatchRecordRes> listWatchedByUserIdAndCourseId(Long userId, Long courseId) {
        ThrowsException.isNull(userId, BaseErrorCode.PARAM, "用户ID");
        ThrowsException.isNull(courseId, BaseErrorCode.PARAM, "课程ID");
        return courseChapterWatchRecordRepository.listWatchedByUserIdAndCourseId(userId, courseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return batchDelete(Collections.singletonList(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> idList) {
        ThrowsException.isTrue(idList == null || idList.isEmpty(), BaseErrorCode.PARAM, "记录主键列表");
        return courseChapterWatchRecordRepository.delete(idList);
    }

    @Override
    public void bufferWatch(CourseChapterRes chapter) {
        if (chapter == null) {
            return;
        }
        Long currentUserId = userAccountApi.currentUserId();
        if (currentUserId == null) {
            return;
        }
        try {
            CourseChapterWatchRecordReq req = new CourseChapterWatchRecordReq();
            req.setUserId(currentUserId);
            req.setCourseId(chapter.getCourseId());
            req.setCourseChapterId(chapter.getId());
            req.setChapterNum(chapter.getChapterNum());
            RedisListUtil.pushLast(RedisEnum.Key.COURSE_CHAPTER_WATCH_BUCKET.getCode(), JSONUtil.toJsonStr(req));
        } catch (Exception e) {
            log.error("章节观看事件写入缓冲队列失败: chapterId={}, userId={}", chapter.getId(), currentUserId, e);
        }
    }

    @Override
    public void syncBucketToDb() {
        int handled = 0;
        while (handled < CourseChapterWatchRedisConstant.SYNC_BATCH_SIZE) {
            String eventJson = RedisListUtil.popFirst(RedisEnum.Key.COURSE_CHAPTER_WATCH_BUCKET.getCode());
            if (eventJson == null) {
                break;
            }
            handled++;
            try {
                CourseChapterWatchRecordReq req = JSONUtil.toBean(eventJson, CourseChapterWatchRecordReq.class);
                saveOrUpdateByEvent(req);
            } catch (Exception e) {
                log.error("章节观看事件落库失败, 跳过该条: {}", eventJson, e);
            }
        }
        if (handled > 0) {
            log.info("章节观看缓冲队列同步数据库完成, 本次处理 {} 条", handled);
        }
    }

    /**
     * 按缓冲事件 upsert 观看记录
     *
     * <p>信任事件携带的 userId(异步落库无当前登录态, 不再取 currentUser), 存在则累加观看次数, 否则新增</p>
     *
     * @param req 观看事件
     */
    private void saveOrUpdateByEvent(CourseChapterWatchRecordReq req) {
        ThrowsException.isNull(req.getUserId(), BaseErrorCode.PARAM, "用户ID");
        ThrowsException.isNull(req.getCourseChapterId(), BaseErrorCode.PARAM, "章节ID");
        CourseChapterWatchRecordRes exist = courseChapterWatchRecordRepository
                .getByUserIdAndChapterId(req.getUserId(), req.getCourseChapterId());
        if (exist != null) {
            courseChapterWatchRecordRepository.addWatchTimes(exist.getId());
            return;
        }
        courseChapterWatchRecordRepository.save(req);
    }
}
