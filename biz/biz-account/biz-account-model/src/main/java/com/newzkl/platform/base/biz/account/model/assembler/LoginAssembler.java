package com.newzkl.platform.base.biz.account.model.assembler;


import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.account.model.res.LoginAccountRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.auth.req.CodeLoginRegisterReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentitySaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.LoginReq;
import com.newzkl.platform.base.biz.account.model.auth.vo.AccountLoginLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 用户账号
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface LoginAssembler {

    @Mappings({
            @Mapping(target = "credential", source = "username")
    })
    AccountQuery passwordLoginReq2Query(LoginReq passwordLoginReq);

    LoginAccountRes vo2LoginRes(AccountVO account);

    AccountLoginLogRes loginLogVO2Res(AccountLoginLogVO it);

    LoginReq codeLoginRegisterReq2LoginReq(CodeLoginRegisterReq codeLoginRegisterReq);

    IdentitySaveReq codeLoginRegisterReq2SaveReq(CodeLoginRegisterReq codeLoginReq);
}
