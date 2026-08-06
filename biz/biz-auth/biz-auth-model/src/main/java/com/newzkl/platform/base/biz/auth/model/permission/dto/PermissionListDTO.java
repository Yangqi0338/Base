package com.newzkl.platform.base.biz.auth.model.permission.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 权限列表数据传输对象
 *
 * <p>作 Controller {@code @RequestBody} 顶层入参, 规避 Spring 对 {@code List<T>} 顶层校验报
 * "[].xxx must not be null"</p>
 *
 * @author KC
 */
public class PermissionListDTO extends ArrayList<PermissionDTO> implements Serializable {
}
