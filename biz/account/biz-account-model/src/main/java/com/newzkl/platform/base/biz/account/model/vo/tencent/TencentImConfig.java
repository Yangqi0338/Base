package com.newzkl.platform.base.biz.account.model.vo.tencent;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sijiwang
 */
@Data
public class TencentImConfig implements Serializable {

    private Long sdkAppId;

    private String secretKey;

    private String identifier;

    private Long expire;
}