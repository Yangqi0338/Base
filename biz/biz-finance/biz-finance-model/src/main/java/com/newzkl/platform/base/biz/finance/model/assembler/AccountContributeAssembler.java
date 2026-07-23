package com.newzkl.platform.base.biz.finance.model.assembler;

import com.newzkl.platform.base.biz.finance.model.earnings.req.AlterAccountContributeDataReq;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AccountContributeRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.AccountContributeVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.EarningContributeRpcVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 银行转换器
 *
 * @author kc
 */
@Mapper(componentModel = "spring", imports = BaseConvert.class)
public interface AccountContributeAssembler extends BaseAssembler<AlterAccountContributeDataReq, AccountContributeVO> {

    List<EarningContributeRpcVO> vo2RpcVO(List<AccountContributeRes> records);

    AccountContributeRes vo2Res(AccountContributeVO accountContributeVO);
}