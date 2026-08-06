package com.newzkl.platform.base.biz.auth.action.cmd;

import java.util.List;

/**
 * 角色绑定账号入参
 *
 * @param accountIds 账号ID集合
 */
public record BindAccountCommand(List<Long> accountIds) {
}
