package com.newzkl.platform.base.biz.course.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.service.CourseChapterDomain;
import com.newzkl.platform.base.biz.course.model.chapter.query.CourseChapterQuery;
import com.newzkl.platform.base.biz.course.model.chapter.req.CourseChapterReq;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程章节管理
 *
 * @author KC
 */
@RestController
@RequestMapping("/course/chapter")
@RequiredArgsConstructor
public class CourseChapterController {

    private final CourseChapterDomain courseChapterDomain;

    /**
     * 新增章节
     *
     * @param req 章节请求
     * @return 章节视图
     */
    @PostMapping("/add")
    public PlatformResult<CourseChapterRes> add(@Validated @RequestBody CourseChapterReq req) {
        if (req.getId() != null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(courseChapterDomain.add(req));
    }

    /**
     * 编辑章节
     *
     * @param req 章节请求
     * @return 章节视图
     */
    @PostMapping("/edit")
    public PlatformResult<CourseChapterRes> edit(@Validated @RequestBody CourseChapterReq req) {
        if (req.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(courseChapterDomain.edit(req));
    }

    /**
     * 启用章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    @PostMapping("/enable/{id}")
    public PlatformResult<Boolean> enable(@PathVariable("id") Long id) {
        return PlatformResult.success(courseChapterDomain.enable(id));
    }

    /**
     * 禁用章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    @PostMapping("/disable/{id}")
    public PlatformResult<Boolean> disable(@PathVariable("id") Long id) {
        return PlatformResult.success(courseChapterDomain.disable(id));
    }

    /**
     * 设为免费章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    @PostMapping("/setFree/{id}")
    public PlatformResult<Boolean> setFree(@PathVariable("id") Long id) {
        return PlatformResult.success(courseChapterDomain.setFree(id));
    }

    /**
     * 设为收费章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    @PostMapping("/setCharge/{id}")
    public PlatformResult<Boolean> setCharge(@PathVariable("id") Long id) {
        return PlatformResult.success(courseChapterDomain.setCharge(id));
    }

    /**
     * 按主键查章节
     *
     * @param id 章节主键
     * @return 章节视图
     */
    @GetMapping("/get/{id}")
    public PlatformResult<CourseChapterRes> get(@PathVariable("id") Long id) {
        return PlatformResult.success(courseChapterDomain.getById(id));
    }

    /**
     * 分页查章节
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<IPage<CourseChapterRes>> page(@RequestBody CourseChapterQuery query) {
        return PlatformResult.success(courseChapterDomain.pageQuery(query));
    }

    /**
     * 按课程编号查章节列表
     *
     * @param courseNum 课程编号
     * @return 章节列表
     */
    @GetMapping("/listByCourse/{courseNum}")
    public PlatformResult<List<CourseChapterRes>> listByCourse(@PathVariable("courseNum") String courseNum) {
        return PlatformResult.success(courseChapterDomain.listByCourseNum(courseNum));
    }

    /**
     * 删除章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    @PostMapping("/delete/{id}")
    public PlatformResult<Boolean> delete(@PathVariable("id") Long id) {
        return PlatformResult.success(courseChapterDomain.delete(id));
    }

    /**
     * 批量删除章节
     *
     * @param idListCommand 主键列表命令
     * @return 是否成功
     */
    @PostMapping("/batchDelete")
    public PlatformResult<Boolean> batchDelete(@Validated @RequestBody IdListCommand idListCommand) {
        return PlatformResult.success(courseChapterDomain.batchDelete(idListCommand.getIdList()));
    }

    /**
     * 恢复已删除章节
     *
     * @param id 章节主键
     * @return 是否成功
     */
    @PostMapping("/recover/{id}")
    public PlatformResult<Boolean> recover(@PathVariable("id") Long id) {
        return PlatformResult.success(courseChapterDomain.recover(id));
    }
}
