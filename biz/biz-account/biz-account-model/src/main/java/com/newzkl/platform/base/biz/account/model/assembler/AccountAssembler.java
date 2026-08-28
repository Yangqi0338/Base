package com.newzkl.platform.base.biz.account.model.assembler;


import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.*;
import com.newzkl.platform.base.biz.account.model.vo.*;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentitySaveReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.expression.spel.ast.Operator;

import java.util.List;

/**
 * 用户账号
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface AccountAssembler extends BaseAssembler<AccountReq, AccountVO> {

    @Mappings({
            @Mapping(target = "yqm", ignore = true),
    })
    IdentitySaveReq do2SaveReq(AccountRes account);

    AppAccountVO account2AppVO(AccountVO accountVO);

    
    
    
    
    AccountOutRes vo2OutRes(AccountVO accountVO);

    AccountQuery identityQuery2Query(IdentityAccountQuery identityAccountQuery);

    AccountQuery simpleQuery2Query(SimpleAccountQuery accountQuery);

    List<AccountStructureTreeVO> structure2TreeList(List<AccountStructureVO> accountStructureVOS);

    AccountRegisterRes vo2RegisterRes(AccountVO account);
}
