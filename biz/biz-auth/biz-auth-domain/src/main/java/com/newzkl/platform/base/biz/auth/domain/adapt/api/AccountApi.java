package com.newzkl.platform.base.biz.auth.domain.adapt.api;

import com.newzkl.platform.base.biz.auth.model.oauth.req.LoginReq;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.facade.AccountRpcVO;

public interface AccountApi {
    AccountRpcVO account(LoginReq loginReq);

    AccountRpcVO account(CommonEnum.Client client, Long accountId);
}
