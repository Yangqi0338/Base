package com.newzkl.platform.base.biz.account.model.support;

import lombok.Data;

import java.io.Serializable;

/**
 * 运营商配置
 */
@Data
public class OperatorConfigVO implements Serializable {

    /**
     * 基础域名
     */
    private String baseDomain;

}
