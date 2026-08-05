package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.facade.AccountFacade;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountApiImpl implements AccountApi {

    @DubboReference
    private AccountFacade accountFacade;

    @Override
    public List<AccountGroupVO> listAccountByIds(List<Long> accountIdList) {
        return accountFacade.listAccountByIds(accountIdList);
    }

    @Override
    public List<Long> queryMember(String nickname) {
        return accountFacade.queryMember(nickname);
    }

    @Override
    public AccountGroupVO selectByUserAccount(String userAccount) {
        return accountFacade.selectByUserAccount(userAccount);
    }

    @Override
    public AccountGroupVO accountInfo(Long accountId) {
        return accountFacade.accountInfo(CommonEnum.Client.USER, accountId);
    }

    @Override
    public AccountGroupVO channelInfo(Long accountId) {
        return accountFacade.accountInfo(CommonEnum.Client.CHANNEL, accountId);
    }
}
