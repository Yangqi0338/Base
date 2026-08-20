package com.newzkl.platform.base.biz.auth.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq;
import com.newzkl.platform.base.common.ddd.facade.AccountRpcVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

import java.util.List;

public interface AccountApi {
    AccountRpcVO account(AccountEnum.Client client, Long accountId);
    AccountRpcVO account(AccountEnum.Client client, String username);
    AccountRpcVO account(String yqm);

    AccountRpcVO register(List<IdentityRegisterRpcReq> registerRpcReq);

    boolean updateLoginTime(Long id);
}
