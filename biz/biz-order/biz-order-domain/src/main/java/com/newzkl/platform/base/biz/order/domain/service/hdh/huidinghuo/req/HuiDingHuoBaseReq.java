package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 会订货API基础请求类
 * 所有会订货API请求类都应继承此类，封装通用字段和基础能力
 */
@Data
public abstract class HuiDingHuoBaseReq {

    /**
     * appId由会订货提供（所有接口必填）
     */
    @NotBlank(message = "appId不能为空")
    private String appId;

    /**
     * 扩展参数（可选，JSON格式字符串）
     */
    private String extendParams;
}