package com.newzkl.platform.base.common.core.model.exception;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

/**
 * @author fang
 */
public interface ErrorCode extends Serializable {

    Integer getCode();
    String getMessage();

    /**
     * 格式化message
     */
    default String getMessage(Object... args) {
        return StrUtil.format(getMessage(), ArrayUtil.map(args, (it) -> it == null ? "" : it.toString()));
    }
}
