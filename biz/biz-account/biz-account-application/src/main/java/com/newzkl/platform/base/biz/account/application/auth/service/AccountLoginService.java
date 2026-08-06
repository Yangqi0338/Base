package com.newzkl.platform.base.biz.account.application.auth.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.req.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.account.model.req.CodeUpdateUsernameReq;
import com.newzkl.platform.base.biz.account.model.req.ResetMemberReq;
import com.newzkl.platform.base.biz.account.model.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.account.model.auth.req.*;
import com.newzkl.platform.base.biz.account.model.auth.res.LoginRes;
import com.newzkl.platform.base.biz.account.model.auth.res.TokenAndExpireRes;

import java.util.List;

/**
 * 账号登录领域服务接口
 */
public interface AccountLoginService {

    /**
     * 账号因素登录
     *
     * @param loginReq 密码登录请求参数
     * @return 登录结果
     */
    LoginRes accountLogin(LoginReq loginReq);

    /**
     * 子账号密码登录
     *
     * @param mainUsername 主账号用户名
     * @param loginReq     密码登录请求参数
     * @return 登录结果
     */
    LoginRes subPasswordLogin(String mainUsername, LoginReq loginReq);

    /**
     * 刷新 Token
     *
     * @param accountId 账号ID
     * @return 登录结果
     */
    LoginRes refreshToken(Long accountId);

    /**
     * 切换客户端
     *
     * @param toggleClientReq 切换客户端请求参数
     * @return 登录结果
     */
    LoginRes toggleClient(ToggleClientReq toggleClientReq);

    /**
     * 登录记录分页
     *
     * @param accountLoginLogQuery
     * @return
     */
    Page<AccountLoginLogRes> accountLoginLogPage(AccountLoginLogQuery accountLoginLogQuery);

    /**
     * 登录注册 集成
     *
     * @param codeLoginRegisterReq
     * @return
     */
    LoginRes loginRegister(CodeLoginRegisterReq codeLoginRegisterReq);

    /**
     * 通过账号ID获取Token及剩余有效期
     *
     * @param accountId 账号ID（即account.getId()）
     * @param client
     * @return TokenAndExpireVO（无有效登录时抛出异常）
     */
    TokenAndExpireRes getTokenAndExpireById(Long accountId, CommonEnum.Client client);

    /**
     * 批量注册
     */
    void accountBatchRegister(List<CustomSaveBatchReq> customSaveBatchReqList);

    /**
     * 批量注册
     */
    Long customeRegister(IdentityCustomSaveReq customSaveReq);

    /**
     * 供应商通过运营商生成的注册链接进行注册
     */
    Long inviteSupplierRegister(IdentityCustomSaveReq customSaveReq, String host);

    /**
     * 重置会员密码 第一步 验证短信
     *
     * <p>迁自旧 {@code IAccountDomain.resetPasswordSmsCode(ResetMemberCommand)}:
     * 解密 {@code sign} → 时间戳有效期 → 限流 → nonce 防重放 (分布式锁内) → 查有效账号 →
     * 校验短信验证码 → 标记 nonce 已用 → 写「设备 + 手机号」验证通过标记。
     * 安全链路逐环保留, 未做简化</p>
     *
     * @param req 找回密码请求 (只读 {@code sign}, 其余明文字段旧代码也不读)
     */
    void resetPasswordSmsCode(ResetMemberReq req);

    /**
     * 重置会员密码 第二步 执行修改密码
     *
     * <p>迁自旧 {@code IAccountDomain.resetMemberPasswordUpdate(ResetMemberCommand)}:
     * 解密 {@code sign} → 时间戳有效期 → 校验第一步留下的验证通过标记 → 查有效账号 →
     * 密码强度校验 (至少 8 位且含字母与数字) → 更新密码 → 删除验证标记</p>
     *
     * @param req 找回密码请求 (只读 {@code sign})
     */
    void resetMemberPasswordUpdate(ResetMemberReq req);
}
