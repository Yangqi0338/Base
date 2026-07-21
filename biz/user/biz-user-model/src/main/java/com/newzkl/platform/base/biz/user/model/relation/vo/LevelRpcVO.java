package com.newzkl.platform.base.biz.user.model.relation.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 等级 RPC 视图对象。
 *
 * @author muc_fang
 */
@Data
public class LevelRpcVO implements Serializable {

    /**
     * ID
     */
    private Long id;
    /**
     * 等级名称
     */
    private String name;
    /**
     * 等级权限
     */
    private PermissionRpcVO permission;
}
