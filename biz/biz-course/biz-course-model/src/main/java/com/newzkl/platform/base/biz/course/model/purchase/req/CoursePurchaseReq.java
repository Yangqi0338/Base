package com.newzkl.platform.base.biz.course.model.purchase.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 课程购买下单入参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.req.CoursePurchaseReq}。
 * {@code jakarta.validation} 换 {@code jakarta.validation}, 语义不变。</p>
 *
 * @author KC
 */
@Data
public class CoursePurchaseReq {

    /**
     * 课程ID(必填)
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /**
     * 支付方式(必填: 1-微信支付, 2-支付宝支付)
     */
    @NotNull(message = "支付方式不能为空")
    private Integer payType;

    /**
     * 用户ID(非必填, 前端未传由登录态覆写)
     */
    private Long userId;
}
