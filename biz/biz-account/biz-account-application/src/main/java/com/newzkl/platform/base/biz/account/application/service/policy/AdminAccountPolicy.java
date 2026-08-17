package com.newzkl.platform.base.biz.account.application.service.policy;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicy;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.res.AccountRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountProxySaveReq;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 平台端账号策略
 *
 * @author muc_fang
 */
@Component
public class AdminAccountPolicy extends AbsAccountPolicy {

    @Override
    public CommonEnum.Client support() {
        return CommonEnum.Client.ADMIN;
    }

    /**
     * 平台端账号注册
     *
     * <p>管理员账号 = AccountDO 行(client=ADMIN, roleIdList 含 PLATFORM(1))。
     * 旧 admin_account 表已合并至 account, 本策略负责 ADMIN 端注册逻辑。</p>
     *
     * @param customSaveReq 账号请求
     * @return 注册结果
     * @author KC
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountRegisterRes customRegister(AccountCustomSaveReq customSaveReq) {
        // 平台账号恒为 PLATFORM 角色, ADMIN 端
        customSaveReq.setRole(RoleEnum.CompanyRole.PLATFORM);
        AccountVO account = super.doRegisterAccount(customSaveReq);
        return accountAssembler.vo2RegisterRes(account);
    }

    /**
     * 平台端账号代理注册
     *
     * <p>迁移说明: 旧实现为 {@code void proxyRegister(String)} 空方法体(静默无操作)。
     * 新签名要求返回 {@code AccountRegisterRes}, 为保持与本类 {@link AdminAccountPolicy#customRegister} 一致的
     * "未实现即返回 null" 语义, 此处返回 null。</p>
     *
     * @param proxySaveReq 代理注册参数
     * @return 恒为 null (未实现)
     * @author KC
     */
    @Override
    public AccountRegisterRes proxyRegister(AccountProxySaveReq proxySaveReq) {
        return null;
    }

	@Override
    public void inviteSuccess(AccountVO account, AccountRes inviteAccount, Object roleObj) {

    }

    @Override
    public Class<?> getEntityClass() {
        return null;
    }
}
