package com.newzkl.platform.base.biz.account.application.service.policy;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicy;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.res.AccountRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountProxySaveReq;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import org.springframework.stereotype.Component;

/**
 * 渠道商端账号策略
 *
 * @author muc_fang
 * @Description: 角色策略
 * @date 2024/1/911:42
 */
@Component
public class ChannelAccountPolicy extends AbsAccountPolicy {

    @Override
    public CommonEnum.Client support() {
        return CommonEnum.Client.CHANNEL;
    }

    @Override
    public AccountRegisterRes customRegister(AccountCustomSaveReq req) {
        // 若是没有昵称 头像 IM账号 默认生成
        req.setNickname(StrUtil.blankToDefault(req.getNickname(), BusinessCodeUtil.generate(BusinessType.DEFAULT_USER_NAME)));
        req.setHeadImg(StrUtil.blankToDefault(req.getHeadImg(), BusinessCodeUtil.generate(BusinessType.DEFAULT_AVATAR)));
        req.setUserAccount(StrUtil.blankToDefault(req.getUserAccount(), BusinessCodeUtil.generate(BusinessType.IM_USER_ACCOUNT)));

        // 用im账号直接作为登录凭证
        String username = req.getUsername();
        if (PhoneUtil.isPhone(username) && StrUtil.isBlank(req.getPhone())) {
            req.setPhone(username);
        }
        req.setUsername(req.getUserAccount());
        AccountVO account = super.doRegisterAccount(req);

        if (!account.isOld()) {
            super.sendTencentImMsg(account);
        }
        return accountAssembler.vo2RegisterRes(account);
    }

    @Override
    public AccountRegisterRes proxyRegister(AccountProxySaveReq proxySaveReq) {

    }

    @Override
    public void roleApplyAuditEvent(AuditEvent auditEvent) {

    }

    @Override
    public void inviteSuccess(AccountVO account, AccountRes inviteAccount, Object roleObj) {

    }

    @Override
    public Class<?> getEntityClass() {
        return null;
    }
}
