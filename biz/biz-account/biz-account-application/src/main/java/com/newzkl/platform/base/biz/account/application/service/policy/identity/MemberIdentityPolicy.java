package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.MemberReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author muc_fang
 * @Description: 运营商角色策略
 * @date 2024/1/911:42
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberIdentityPolicy extends AbsIdentityPolicy {

    @Autowired
    private UserClientDomain userDomain;

    @Override
    public AccountEnum.Identity support() {
        return AccountEnum.Identity.MEMBER;
    }

    @Override
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        Long accountId = customSaveReq.getId();
        //注册角色
        MemberReq memberCommand = new MemberReq();
        memberCommand.setId(accountId);
        memberCommand.setChannelId(accountId);
        userDomain.memberSave(memberCommand);

        return new IdentityRegisterRes(null, accountId);
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
