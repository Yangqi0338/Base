package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取签名请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetSignReq extends HuiDingHuoBaseReq {

    /**
     * 签名类型
     * MD5：MD5签名
     * SHA256：SHA256签名
     * 默认：MD5
     */
    private String signType;

    /**
     * 签名有效期（秒）
     * 默认：300秒
     */
    private Integer expireTime;
}
