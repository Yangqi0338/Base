package com.newzkl.platform.base.biz.account.model.auth.vo;


import com.newzkl.platform.base.biz.account.model.enums.AuthEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能点
 */
@Data
public class FunctionVO {
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口路径
     */
    private String urlPath;

    /**
     * 接口类型
     */
    private String urlMethod;

    /**
     * 类型
     *
     * @see AuthEnum.FunctionType
     */
    private String type;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 修改人id
     */
    private Long menderId;
}
