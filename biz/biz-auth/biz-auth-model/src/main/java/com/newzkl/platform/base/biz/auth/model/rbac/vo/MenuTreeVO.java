package com.newzkl.platform.base.biz.auth.model.rbac.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 功能点树形结构VO
 */
@Data
public class MenuTreeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 是否已分配给角色（true:已分配，false:未分配）
     */
    private Boolean assigned = false;

    /**
     * 子菜单列表
     */
    private List<MenuTreeVO> childMenu;

    /**
     * 子权限列表
     */
    private List<FunctionVO> childFunction;

    @Data
    public static class FunctionVO {

        /** 主键ID */
        private Long id;

        /**
         * 接口名称
         */
        private String name;

        /**
         * 是否已分配给角色（true:已分配，false:未分配）
         */
        private Boolean assigned = false;
    }
}
