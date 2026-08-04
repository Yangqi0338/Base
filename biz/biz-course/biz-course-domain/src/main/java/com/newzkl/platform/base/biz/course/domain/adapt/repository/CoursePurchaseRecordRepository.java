package com.newzkl.platform.base.biz.course.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.purchase.entity.CoursePurchaseRecord;
import com.newzkl.platform.base.biz.course.model.purchase.query.UserPurchasedCoursePageReq;

import java.util.List;

/**
 * 课程购买记录仓储端口
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.repository.CoursePurchaseRecordRepository}。
 * 源联表方法 {@code pageUserPurchasedCourse} 下沉为单表分页(收发实体),
 * 课程字段装配上移领域层, 保持仓储单一持久化动作。</p>
 *
 * @author KC
 */
public interface CoursePurchaseRecordRepository {

    /**
     * 保存购买记录
     *
     * @param record 购买记录实体
     * @return 保存后的实体(回填主键)
     */
    CoursePurchaseRecord saveRecord(CoursePurchaseRecord record);

    /**
     * 按订单编号查购买记录
     *
     * @param orderNo 订单编号
     * @return 购买记录, 不存在返回 null
     */
    CoursePurchaseRecord findByOrderNo(Long orderNo);

    /**
     * 按用户与课程查支付成功的购买记录
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 购买记录, 不存在返回 null
     */
    CoursePurchaseRecord findSuccessByUserIdAndCourseId(Long userId, Long courseId);

    /**
     * 更新支付状态(支付成功回写流水号与支付时间)
     *
     * @param orderNo  订单编号
     * @param payState 目标支付状态
     * @param payNo    第三方支付流水号
     * @return 是否更新成功
     */
    boolean updatePayState(Long orderNo, Integer payState, String payNo);

    /**
     * 分页查用户购买记录(单表, 仅购买记录字段)
     *
     * <p>课程标题/分类过滤经 {@code courseIdFilter} 预解析后按课程ID集合收窄,
     * 空集合表示过滤命中为空。传 null 表示不按课程收窄。</p>
     *
     * @param req            分页查询条件
     * @param courseIdFilter 预解析课程ID集合, null 表示不收窄
     * @return 分页结果(仅购买记录字段, 课程字段待领域层装配)
     */
    Page<CoursePurchaseRecord> pageUserPurchased(UserPurchasedCoursePageReq req, List<Long> courseIdFilter);
}
