package com.newzkl.platform.base.biz.auth.model.rbac.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口
 */
@Data
public class InterfaceVO implements Serializable {

    private static final long serialVersionUID = 2584605102625960057L;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 请求方式
     */
    private String urlMethod;

    /**
     * 接口路径
     */
    private String urlPath;

}