package com.newzkl.platform.base.biz.auth.model.rbac.req;

import lombok.Data;

/**
 * 菜单
 */
@Data
public class MenuReq {

    private Long id;

    /**
     * 隶属菜单id
     */
    private String affiliatedMenuId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 修改人id
     */
    private Long menderId;

    /**
     * 系统id
     */
    private Long systemId;

    /**
     * 排序值
     */
    private Integer sort;

}
