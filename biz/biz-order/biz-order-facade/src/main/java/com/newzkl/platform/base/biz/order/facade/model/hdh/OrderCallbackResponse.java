package com.newzkl.platform.base.biz.order.facade.model.hdh;

import lombok.Data;

/**
 * 订单回调接口响应参数
 */
@Data
public class OrderCallbackResponse {
    /**
     * 处理结果：1-成功，0-失败
     */
    private Integer success;
}