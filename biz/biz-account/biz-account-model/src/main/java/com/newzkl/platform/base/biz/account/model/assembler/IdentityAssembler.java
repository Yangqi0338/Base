package com.newzkl.platform.base.biz.account.model.assembler;


import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountProxySaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import org.mapstruct.Mapper;

/**
 * 用户账号
 *
 * @author fang
 */
@Mapper(componentModel = "spring", uses = {BaseConvert.class})
public interface IdentityAssembler {

    AccountProxySaveReq identityProxyReq2AccountProxyReq(IdentityProxySaveReq proxySaveReq);
}
