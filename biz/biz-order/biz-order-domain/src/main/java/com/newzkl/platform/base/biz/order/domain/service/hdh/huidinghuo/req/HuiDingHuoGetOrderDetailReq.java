package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.AssertTrue;

/**
 * 获取订单详情请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetOrderDetailReq extends HuiDingHuoBaseReq {

    /**
     * 系统订单号（与userOrderNum二选一）
     */
    private String orderNum;

    /**
     * 用户订单号（与orderNum二选一）
     */
    private String userOrderNum;

    /**
     * 校验：orderNum和userOrderNum至少填一项
     */
    @AssertTrue(message = "orderNum和userOrderNum至少需填写一项")
    public boolean isOrderNumValid() {
        return (orderNum != null && !orderNum.isEmpty()) || (userOrderNum != null && !userOrderNum.isEmpty());
    }
}