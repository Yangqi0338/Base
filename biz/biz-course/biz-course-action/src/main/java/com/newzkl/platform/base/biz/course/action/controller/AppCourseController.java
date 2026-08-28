package com.newzkl.platform.base.biz.course.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.service.CourseDomain;
import com.newzkl.platform.base.biz.course.domain.service.CoursePurchaseRecordDomain;
import com.newzkl.platform.base.biz.course.model.course.query.CourseQuery;
import com.newzkl.platform.base.biz.course.model.course.res.AppCourseRes;
import com.newzkl.platform.base.biz.course.model.course.res.CourseRes;
import com.newzkl.platform.base.biz.course.model.purchase.query.UserPurchasedCoursePageReq;
import com.newzkl.platform.base.biz.course.model.purchase.req.CoursePurchaseReq;
import com.newzkl.platform.base.biz.course.model.purchase.res.CoursePurchaseCreateRes;
import com.newzkl.platform.base.biz.course.model.purchase.res.UserPurchasedCourseRes;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * C 端课程内容查询 + 课程购买
 *
 * <p>课程内容读端点 + 课程购买(下单/我的已购)。课程购买按业务概念归属 biz-course
 * (虚拟商品/服务订单为技术概念非业务概念, 不建 benefit-order 域)。购买下单/已购列表
 * 的 userId 均由登录态覆写, 不信任前端入参。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/appCourse")
@RequiredArgsConstructor
@FuncPermission("C 端课程内容查询 + 课程购买")
public class AppCourseController {

    private final CourseDomain courseDomain;

    private final CoursePurchaseRecordDomain coursePurchaseRecordDomain;

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

    /**
     * 课程购买下单
     *
     * <p>userId 由登录态覆写, 忽略前端入参。售价为 0 直接置支付成功,
     * 否则发起支付并返回支付二维码。</p>
     *
     * @param req 购买下单入参
     * @return 下单出参(含支付二维码)
     */
    @PostMapping("/create")
    @FuncPermission("课程购买下单")
    public PlatformResult<CoursePurchaseCreateRes> create(@Valid @RequestBody CoursePurchaseReq req) {
        req.setUserId(SecurityUtils.getAccountId());
        return PlatformResult.success(coursePurchaseRecordDomain.createCoursePurchaseRecord(req));
    }

    /**
     * 分页查我的已购课程
     *
     * <p>userId 由登录态覆写, 只返回当前用户的购买记录。</p>
     *
     * @param req 分页查询条件
     * @return 分页结果(装配课程/讲师/分类/观看统计)
     */
    @PostMapping("/pageUserPurchased")
    public PlatformResult<IPage<UserPurchasedCourseRes>> pageUserPurchased(@RequestBody UserPurchasedCoursePageReq req) {
        req.setUserId(SecurityUtils.getAccountId());
        return PlatformResult.success(coursePurchaseRecordDomain.pageUserPurchasedCourse(req));
    }
}
