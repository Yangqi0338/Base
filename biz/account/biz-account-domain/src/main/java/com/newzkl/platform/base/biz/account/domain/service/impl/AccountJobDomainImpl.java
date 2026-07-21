package com.newzkl.platform.base.biz.account.domain.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.AccountJobRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountJobDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountJobQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountJobReq;
import com.newzkl.platform.base.biz.account.model.res.AccountJobRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountJobVO;
import com.newzkl.platform.base.biz.account.model.assembler.AccountJobAssembler;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 后台角色
 *
 * @author fang
 */
@Service
@RequiredArgsConstructor
public class AccountJobDomainImpl implements AccountJobDomain {

    private final AccountJobRepository accountJobRepository;
    private final AccountJobAssembler assembler;

    @Override
    public Long save(AccountJobReq req) {
        return accountJobRepository.save(assembler.req2VO(req));
    }

    @Override
    public void delete(List<Long> idList) {
        accountJobRepository.delete(idList);
    }

    @Override
    public AccountJobVO detail(Long id) {
        return accountJobRepository.detail(id);
    }

    @Override
    public Page<AccountJobRes> accountJobPageVO(AccountJobQuery accountJobQuery) {
        return TransferUtils.transferPage(accountJobRepository.pageList(accountJobQuery), assembler::vo2Res);
    }
}
