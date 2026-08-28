package com.newzkl.platform.base.biz.account.action.cmd;

import java.util.List;

/**
 * 账号绑定角色入参
 *
 * <p>对应 adopt-chicken {@code EmpController#bindRoles} 的裸 {@code List<Long>} 请求体,
 * 中台统一包一层命令对象, 与 {@code BindAccountCommand} 风格对齐。</p>
 *
 * @param roleIds 角色ID集合, 空集视为清空
 * @author KC
 */
public record BindRoleCommand(List<Long> roleIds) {
}
