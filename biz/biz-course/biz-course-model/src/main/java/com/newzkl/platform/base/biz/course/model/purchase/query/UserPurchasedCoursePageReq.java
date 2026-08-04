package com.newzkl.platform.base.biz.course.model.purchase.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户已购课程分页查询入参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.req.UserPurchasedCoursePageReq}
 * (源直接继承 mybatis-plus {@code Page}, 新架构统一继承 {@code BizPageQuery})。
 * 源 {@code includeDeleted} 去除: 逻辑删除由 {@code BaseDO#delFlag} + {@code @TableLogic} 自动过滤。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserPurchasedCoursePageReq extends BizPageQuery {

    /**
     * 用户ID(筛选当前用户的购买记录, 由登录态覆写)
     */
    private Long userId;

    /**
     * 课程标题, 模糊检索
     */
    private String courseTitle;

    /**
     * 课程分类ID, 精确检索
     */
    private Long categoryId;

    /**
     * 支付状态: 0-待支付, 1-支付成功, 2-支付失败; 为空查全部
     */
    private Integer payState;
}
