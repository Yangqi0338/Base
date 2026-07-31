package com.newzkl.platform.base.biz.course.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.service.CourseDomain;
import com.newzkl.platform.base.biz.course.model.course.query.CourseQuery;
import com.newzkl.platform.base.biz.course.model.course.res.AppCourseRes;
import com.newzkl.platform.base.biz.course.model.course.res.CourseRes;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * C 端课程内容查询
 *
 * <p>仅承载课程内容读端点, 课程购买(下单/我的已购)不在本控制器, 归 benefit-order 域。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/appCourse")
@RequiredArgsConstructor
public class AppCourseController {

    private final CourseDomain courseDomain;

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
     * @param courseNum 课程编号
     * @return 课程视图
     */
    @GetMapping("/getByNum/{courseNum}")
    public PlatformResult<CourseRes> getByNum(@PathVariable("courseNum") String courseNum) {
        return PlatformResult.success(courseDomain.getByCourseNum(courseNum));
    }

    /**
     * C 端课程分页, 含讲师/分类/章节聚合信息
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<IPage<AppCourseRes>> page(@RequestBody CourseQuery query) {
        return PlatformResult.success(courseDomain.appPageQuery(query));
    }
}
