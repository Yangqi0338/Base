package com.newzkl.platform.base.biz.auth.action.cmd;

import java.util.List;

/**
 * 角色分配权限入参
 *
 * @param permissionIds 权限ID集合
 */
public record AssignPermissionCommand(List<Long> permissionIds) {
}
