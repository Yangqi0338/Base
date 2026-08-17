package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicySupport;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.MemberReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountProxySaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author muc_fang
 * @Description: 运营商角色策略
 * @date 2024/1/911:42
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberIdentityPolicy extends AbsIdentityPolicy {
    private final UserQueryService roleQueryAppService;
    private final UserClientDomain memberDomain;

    @Override
    public RoleEnum.CompanyRole support() {
        return RoleEnum.CompanyRole.MEMBER;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        // 注册用户端账号
        AccountCustomSaveReq accountCustomSaveReq = identityAssembler.identityCustomReq2AccountCustomReq(customSaveReq);
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .customRegister(accountCustomSaveReq);

        Long accountId = account.getId();
        //检查角色
        MemberVO memberVO = roleQueryAppService.memberVO(accountId);
        if (memberVO != null) {
            return new IdentityRegisterRes(AccountErrorCode.EXIST_ROLE, accountId);
        }

        //注册角色
        MemberReq memberCommand = new MemberReq();
        memberCommand.setId(accountId);
        memberCommand.setName(customSaveReq.getNickname());
        memberDomain.memberSave(memberCommand);

        accountService.roleAddEvent(account, RoleEnum.CompanyRole.MEMBER, customSaveReq.getPassword());
        return new IdentityRegisterRes(null, accountId);
    }

    @Override
    public IdentityRegisterRes proxyRegister(IdentityProxySaveReq proxySaveReq) {
        // 代理注册账号
        AccountProxySaveReq accountProxySaveReq = identityAssembler.identityProxyReq2AccountProxyReq(proxySaveReq);
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .proxyRegister(accountProxySaveReq);

        Long accountId = account.getId();
        //检查角色
        MemberVO memberVO = roleQueryAppService.memberVO(accountId);

        //注册角色
        if (StrUtil.isNotBlank(proxySaveReq.getNickname())) {

        }
        MemberReq memberCommand = new MemberReq();
        memberCommand.setId(accountId);
        memberCommand.setName(proxySaveReq.getNickname());

        if (memberVO != null) {
            //修改角色
            memberDomain.memberEdit(accountId, memberCommand);
        } else {
            memberDomain.memberSave(memberCommand);
        }


        accountService.roleAddEvent(account, RoleEnum.CompanyRole.MEMBER, proxySaveReq.getPassword());
        return new IdentityRegisterRes(null, accountId);
    }


    @Override
    public void inviteSuccess(AccountVO inviteAccount, AccountVO account, Object roleObj) {

    }

    @Override
    public Object detail(Long id) {
        return null;
    }

    @Override
    public boolean destroy(AccountVO accountVO, String destroyReason) {
        return false;
    }

    @Override
    public void saveByAccount(AccountReq req) {

    }
}
