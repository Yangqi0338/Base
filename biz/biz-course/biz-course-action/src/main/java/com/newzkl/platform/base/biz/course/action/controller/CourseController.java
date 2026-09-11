package com.newzkl.platform.base.biz.course.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.service.CourseDomain;
import com.newzkl.platform.base.biz.course.model.course.query.CourseQuery;
import com.newzkl.platform.base.biz.course.model.course.req.CourseDetailReq;
import com.newzkl.platform.base.biz.course.model.course.req.CourseReq;
import com.newzkl.platform.base.biz.course.model.course.res.CourseRes;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.req.IdCommand;
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
 * 课程管理
 *
 * @author KC
 */
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
@FuncPermission("课程管理")
public class CourseController {

    private final CourseDomain courseDomain;

    /**
     * 新增课程
     *
     * @param req 课程基础请求
     * @return 课程视图
     */
    @PostMapping("/add")
    @FuncPermission("新增课程")
    public PlatformResult<CourseRes> add(@Validated @RequestBody CourseReq req) {
        if (req.getId() != null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(courseDomain.add(req));
    }

    /**
     * 编辑课程基础信息
     *
     * @param req 课程基础请求
     * @return 课程视图
     */
    @PostMapping("/editBase")
    @FuncPermission("编辑课程基础信息")
    public PlatformResult<CourseRes> editBase(@Validated @RequestBody CourseReq req) {
        if (req.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(courseDomain.editBase(req));
    }

    /**
     * 编辑课程富文本详情
     *
     * @param req 课程详情请求
     * @return 课程视图
     */
    @PostMapping("/editDetail")
    @FuncPermission("编辑课程富文本详情")
    public PlatformResult<CourseRes> editDetail(@Validated @RequestBody CourseDetailReq req) {
        if (req.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(courseDomain.editDetail(req));
    }

    /**
     * 上架课程
     *
     * @param id 课程主键
     * @return 是否成功
     */
    @PostMapping("/enable/{id}")
    @FuncPermission("上架课程")
    public PlatformResult<Boolean> enable(@PathVariable("id") Long id) {
        return PlatformResult.success(courseDomain.enable(id));
    }

    /**
     * 下架课程
     *
     * @param id 课程主键
     * @return 是否成功
     */
    @PostMapping("/disable/{id}")
    @FuncPermission("下架课程")
    public PlatformResult<Boolean> disable(@PathVariable("id") Long id) {
        return PlatformResult.success(courseDomain.disable(id));
    }

    /**
     * 按主键查课程
     *
     * @param id 课程主键
     * @return 课程视图
     */
    @GetMapping("/get/{id}")
    public PlatformResult<CourseRes> get(@PathVariable("id") Long id) {
        return PlatformResult.success(courseDomain.getById(id));
    }

    /**
     * 按课程编号查课程
     *
     * @param courseNo 课程编号
     * @return 课程视图
     */
    @GetMapping("/getByNum/{courseNo}")
    public PlatformResult<CourseRes> getByNum(@PathVariable("courseNo") String courseNo) {
        return PlatformResult.success(courseDomain.getByCourseNo(courseNo));
    }

    /**
     * 分页查课程
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<IPage<CourseRes>> page(@RequestBody CourseQuery query) {
        return PlatformResult.success(courseDomain.pageQuery(query));
    }

    /**
     * 按分类查课程
     *
     * @param categoryId 分类主键
     * @return 课程列表
     */
    @GetMapping("/listByCategory/{categoryId}")
    public PlatformResult<List<CourseRes>> listByCategory(@PathVariable("categoryId") Long categoryId) {
        return PlatformResult.success(courseDomain.listByCategoryId(categoryId));
    }

    /**
     * 删除课程
     *
     * @param id 课程主键
     * @return 是否成功
     */
    @PostMapping("/delete/{id}")
    @FuncPermission("删除课程")
    public PlatformResult<Boolean> delete(@PathVariable("id") Long id) {
        return PlatformResult.success(courseDomain.delete(id));
    }

    /**
     * 批量删除课程
     *
     * @param idListCommand 主键列表命令
     * @return 是否成功
     */
    @PostMapping("/batchDelete")
    @FuncPermission("批量删除课程")
    public PlatformResult<Boolean> batchDelete(@Validated @RequestBody IdCommand idListCommand) {
        return PlatformResult.success(courseDomain.batchDelete(idListCommand.getIdList()));
    }

    /**
     * 恢复已删除课程
     *
     * @param id 课程主键
     * @return 是否成功
     */
    @PostMapping("/recover/{id}")
    @FuncPermission("恢复已删除课程")
    public PlatformResult<Boolean> recover(@PathVariable("id") Long id) {
        return PlatformResult.success(courseDomain.recover(id));
    }
}
