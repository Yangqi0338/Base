package com.newzkl.platform.base.biz.order.facade.model.hdh;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 订单回调接口请求参数
 */
@Data
public class OrderCallbackRequest implements Serializable {
    /**
     * 订单状态（必填）
     */
    @NotBlank(message = "orderStatus不能为空")
    private String orderStatus;

    /**
     * 订单状态code（必填）
     */
    @NotNull(message = "orderStatusCode不能为空")
    private Integer orderStatusCode;

    /**
     * 订单号（必填）
     */
    @NotBlank(message = "orderNum不能为空")
    private String orderNum;

    /**
     * 用户订单号（非必填）
     */
    private String userOrderNum;

    /**
     * 包裹信息列表（必填，至少一个包裹）
     */
    @NotEmpty(message = "pkgList不能为空")
    private List<PkgInfo> pkgList;
}