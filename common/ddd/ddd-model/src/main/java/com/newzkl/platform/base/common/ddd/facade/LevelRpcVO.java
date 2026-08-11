package com.newzkl.platform.base.common.ddd.facade;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: 甄选师等级值对象
 * @date 2023/12/1814:57
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
