package com.newzkl.platform.base.biz.auth.model.assembler;



import com.newzkl.platform.base.biz.auth.model.oauth.dto.AccountLoginLogDTO;
import com.newzkl.platform.base.biz.auth.model.oauth.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.auth.model.oauth.res.LoginAccountRes;
import com.newzkl.platform.base.common.ddd.facade.AccountRpcVO;
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

//    @Mappings({
//            @Mapping(target = "credential", source = "username")
//    })
//    AccountQuery passwordLoginReq2Query(LoginReq passwordLoginReq);

    LoginAccountRes vo2LoginRes(AccountRpcVO account);

    AccountLoginLogRes loginLogVO2Res(AccountLoginLogDTO it);
}
