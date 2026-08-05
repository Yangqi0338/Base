package com.newzkl.platform.base.biz.course.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.service.CourseChapterWatchRecordDomain;
import com.newzkl.platform.base.biz.course.model.watch.query.CourseChapterWatchRecordQuery;
import com.newzkl.platform.base.biz.course.model.watch.req.CourseChapterWatchRecordReq;
import com.newzkl.platform.base.biz.course.model.watch.res.CourseChapterWatchRecordRes;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程章节观看记录管理
 *
 * @author KC
 */
@RestController
@RequestMapping("/courseChapterWatchRecord")
@RequiredArgsConstructor
public class CourseChapterWatchRecordController {

    private final CourseChapterWatchRecordDomain courseChapterWatchRecordDomain;

    /**
     * 保存或更新当前用户观看记录
     *
     * @param req 观看记录请求
     * @return 观看记录视图
     */
    @PostMapping("/saveOrUpdate")
    public PlatformResult<CourseChapterWatchRecordRes> saveOrUpdate(
            @Validated @RequestBody CourseChapterWatchRecordReq req) {
        req.setUserId(SecurityUtils.getAccountId());
        return PlatformResult.success(courseChapterWatchRecordDomain.saveOrUpdate(req));
    }

    /**
     * 按用户与章节查观看记录
     *
     * @param userId 用户主键
     * @param courseChapterId 章节主键
     * @return 观看记录视图
     */
    @GetMapping("/getByUserIdAndChapterId")
    public PlatformResult<CourseChapterWatchRecordRes> getByUserIdAndChapterId(
            @RequestParam("userId") Long userId,
            @RequestParam("courseChapterId") Long courseChapterId) {
        return PlatformResult.success(
                courseChapterWatchRecordDomain.getByUserIdAndChapterId(userId, courseChapterId));
    }

    /**
     * 分页查观看记录
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<IPage<CourseChapterWatchRecordRes>> page(
            @RequestBody CourseChapterWatchRecordQuery query) {
        return PlatformResult.success(courseChapterWatchRecordDomain.pageQuery(query));
    }

    /**
     * 按用户与课程查已观看章节记录
     *
     * @param userId 用户主键
     * @param courseId 课程主键
     * @return 观看记录列表
     */
    @GetMapping("/listWatchedByUserIdAndCourseId")
    public PlatformResult<List<CourseChapterWatchRecordRes>> listWatchedByUserIdAndCourseId(
            @RequestParam("userId") Long userId,
            @RequestParam("courseId") Long courseId) {
        return PlatformResult.success(
                courseChapterWatchRecordDomain.listWatchedByUserIdAndCourseId(userId, courseId));
    }

    /**
     * 删除观看记录
     *
     * @param id 观看记录主键
     * @return 是否成功
     */
    @PostMapping("/delete/{id}")
    public PlatformResult<Boolean> delete(@org.springframework.web.bind.annotation.PathVariable("id") Long id) {
        return PlatformResult.success(courseChapterWatchRecordDomain.delete(id));
    }

    /**
     * 批量删除观看记录
     *
     * @param idListCommand 主键列表命令
     * @return 是否成功
     */
    @PostMapping("/batchDelete")
    public PlatformResult<Boolean> batchDelete(@Validated @RequestBody IdListCommand idListCommand) {
        return PlatformResult.success(courseChapterWatchRecordDomain.batchDelete(idListCommand.getIdList()));
    }
}
