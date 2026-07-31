package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取签名响应类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoSignRes extends HuiDingHuoBaseRes {

    /**
     * 签名值
     */
    private String sign;

    /**
     * 签名有效期（秒）
     */
    private Long expireTime;

    /**
     * 签名算法
     */
    private String signAlgorithm;
}
