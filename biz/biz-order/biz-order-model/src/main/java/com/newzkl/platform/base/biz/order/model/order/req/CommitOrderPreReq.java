package com.newzkl.platform.base.biz.order.model.order.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 提交订单预处理参数
 *
 * @author sijiwang
 */
@Data
public class CommitOrderPreReq {

    /**
     * 订单号
     */
    @NotNull(message = "订单号不能为空")
    private String orderNo;

    /**
     * 登录账号id
     */
    private Long accountId;
}
