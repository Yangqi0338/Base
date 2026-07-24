package com.newzkl.platform.base.common.core.job.model;

import lombok.Data;

/**
 * XXL-Job admin REST 通用返回结构
 */
@Data
public class XxlJobReturnT<T> {
    private Integer code;
    private String msg;
    private T content;

    public static final int CODE_SUCCESS = 200;
}
