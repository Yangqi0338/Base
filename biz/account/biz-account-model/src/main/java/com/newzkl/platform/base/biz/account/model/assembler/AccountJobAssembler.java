package com.newzkl.platform.base.biz.account.model.assembler;

import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.biz.account.model.req.AccountJobReq;
import com.newzkl.platform.base.biz.account.model.res.AccountJobRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountJobVO;
import org.mapstruct.Mapper;

/**
 * 职位转换器
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface AccountJobAssembler extends BaseAssembler<AccountJobReq, AccountJobVO> {

    AccountJobRes vo2Res(AccountJobVO it);
}
