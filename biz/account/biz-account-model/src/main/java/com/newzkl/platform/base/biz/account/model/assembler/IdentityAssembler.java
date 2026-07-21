package com.newzkl.platform.base.biz.account.model.assembler;


import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountProxySaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import org.mapstruct.Mapper;

/**
 * 用户账号
 *
 * @author fang
 */
@Mapper(componentModel = "spring", uses = {BaseConvert.class})
public interface IdentityAssembler {

    AccountCustomSaveReq identityCustomReq2AccountCustomReq(IdentityCustomSaveReq customSaveReq);

    AccountProxySaveReq identityProxyReq2AccountProxyReq(IdentityProxySaveReq proxySaveReq);
}
