package com.newzkl.platform.base.biz.finance.domain.earnings.service.impl;


import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountContributeRepository;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.AccountContributeDomain;
import com.newzkl.platform.base.biz.finance.model.assembler.AccountContributeAssembler;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AccountContributeQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AccountContributeRpcQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AlterAccountContributeDataReq;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AccountContributeRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.AccountContributeVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2024/1/25 10:52
 */
@Service
@RequiredArgsConstructor
public class AccountContributeDomainImpl implements AccountContributeDomain {

    private final AccountContributeRepository accountContributeRepository;
    private final AccountContributeAssembler assembler;

    @Override
    public void init(AccountContributeVO req) {
        accountContributeRepository.init(req);
    }

    @Override
    public void alterAccountContribute(List<AlterAccountContributeDataReq> req) {
        accountContributeRepository.alterAccountContribute(req);
    }

    @Override
    public List<AccountContributeRes> queryAccountContribute(AccountContributeQuery req) {
        return TransferUtils.transfers(accountContributeRepository.queryAccountContribute(req), assembler::vo2Res);
    }

    /**
     * 批量查询客户贡献值
     *
     * @param req 批量贡献值查询
     * @return 客户贡献数据列表
     */
    @Override
    public List<AccountContributeRes> batchQueryAccountContribute(AccountContributeRpcQuery req) {
        return TransferUtils.transfers(accountContributeRepository.batchQueryAccountContribute(req), assembler::vo2Res);
    }
}
