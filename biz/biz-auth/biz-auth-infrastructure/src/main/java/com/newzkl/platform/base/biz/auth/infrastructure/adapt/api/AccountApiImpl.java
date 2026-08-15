package com.newzkl.platform.base.biz.auth.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.auth.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.auth.model.oauth.req.LoginReq;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.facade.AccountRpcVO;
import org.springframework.stereotype.Service;

@Service
public class AccountApiImpl implements AccountApi {
    @Override
    public AccountRpcVO account(LoginReq loginReq) {
        return null;
    }

    @Override
    public AccountRpcVO account(CommonEnum.Client client, Long accountId) {
        return null;
    }
}
