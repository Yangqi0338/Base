package com.newzkl.platform.base.biz.course.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.model.purchase.query.UserPurchasedCoursePageReq;
import com.newzkl.platform.base.biz.course.model.purchase.req.CoursePurchaseReq;
import com.newzkl.platform.base.biz.course.model.purchase.res.CoursePurchaseCreateRes;
import com.newzkl.platform.base.biz.course.model.purchase.res.UserPurchasedCourseRes;

/**
 * 课程购买记录领域服务
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.service.CoursePurchaseRecordDomainService}
 * 与应用服务 {@code CoursePurchaseRecordServiceImpl} 的合并结果。课程内容与购买记录同域,
 * 已购列表的课程/讲师/分类/观看字段由本域直接装配, 不经出站端口(slug10 CourseQueryApi 已废)。</p>
 *
 * @author KC
 */
public interface CoursePurchaseRecordDomain {

    /**
     * 创建课程购买记录
     *
     * <p>用户+课程维度分布式锁防并发; 售价为 0 直接置支付成功, 否则发起支付取二维码。
     * 实际购买数递增在支付成功回调({@link #paySuccess}), 免费课在创建即递增。</p>
     *
     * @param req 购买下单入参(userId 由登录态覆写)
     * @return 下单出参(含支付二维码)
     */
    CoursePurchaseCreateRes createCoursePurchaseRecord(CoursePurchaseReq req);

    /**
     * 分页查用户已购课程(装配课程/讲师/分类/观看统计)
     *
     * @param req 分页查询条件
     * @return 分页结果
     */
    IPage<UserPurchasedCourseRes> pageUserPurchasedCourse(UserPurchasedCoursePageReq req);

    /**
     * 支付成功回调(支付回写状态 + 实际购买数递增)
     *
     * <p>消费方 = biz-finance 支付回调, 经 {@code CoursePurchaseFacade#paySuccess} 触达。</p>
     *
     * @param orderNo 订单编号
     * @param payNo   第三方支付流水号
     */
    void paySuccess(Long orderNo, String payNo);
}
