package com.newzkl.platform.base.biz.sys.model.adminaccount.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 密码修改请求对象。
 *
 * <p>[AUTH] {@code accountId} 由入口 starter 依据当前登录态注入 (旧
 * {@code SecurityUtils.getAccountId()} 已下沉), biz 侧作为显式入参接收。</p>
 *
 * @author KC
 */
@Data
public class PasswordUpdateReq {

    /**
     * 账号 id (当前登录 id, 入口注入)。
     */
    @NotNull
    private Long accountId;

    /**
     * 原密码。
     */
    @NotBlank
    private String password;

    /**
     * 新密码。
     */
    @NotBlank
    private String newPassword;
}
