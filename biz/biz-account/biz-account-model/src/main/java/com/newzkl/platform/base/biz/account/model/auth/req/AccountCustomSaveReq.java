package com.newzkl.platform.base.biz.account.model.auth.req;

import lombok.Data;

/**
 * 身份自行注册请求参数
 *
 * @author muc_fang
 * @Description:
 * @date 2024/2/2211:22
 */
@Data
public class AccountCustomSaveReq extends AccountSaveReq {

    /**
     * 代理注册默认验证码。
     *
     * <p>迁移说明: 原 {@code AbsRolePolicy#proxyRegister} 通过
     * {@code super.doRegisterAccount(username, "147852")} 传入的硬编码验证码,
     * 语义为"平台代理注册免验证码", 此处原样保留以维持行为一致。</p>
     */
    private static final String PROXY_REGISTER_CODE = "147852";

    /**
     * 构建代理注册用的账号注册参数。
     *
     * <p>迁移说明: 原实现为策略基类的 {@code doRegisterAccount(String username, String code)}
     * (protected, 仅内部可用)。新结构下身份策略需跨对象调用账号策略的
     * {@code customRegister(AccountCustomSaveReq)}, 故提取为静态工厂,
     * 行为与旧的 {@code doRegisterAccount(username, "147852")} 等价。</p>
     *
     * @param username 登录凭证 (手机号或账号)
     * @return 账号注册参数
     * @author KC
     */
    public static AccountCustomSaveReq proxyRegister(String username) {
        AccountCustomSaveReq req = new AccountCustomSaveReq();
        req.setUsername(username);
        req.setCode(PROXY_REGISTER_CODE);
        return req;
    }
}
