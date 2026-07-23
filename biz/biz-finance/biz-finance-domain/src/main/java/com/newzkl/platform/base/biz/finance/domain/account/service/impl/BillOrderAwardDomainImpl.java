package com.newzkl.platform.base.biz.finance.domain.account.service.impl;

import com.newzkl.platform.base.biz.finance.domain.adapt.repository.BillOrderAwardRepository;
import com.newzkl.platform.base.biz.finance.domain.account.service.BillOrderAwardDomain;
import com.newzkl.platform.base.biz.finance.model.account.req.BillOrderAwardReq;
import com.newzkl.platform.base.biz.finance.model.assembler.BillOrderAwardAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillOrderAwardDomainImpl implements BillOrderAwardDomain {

    private final BillOrderAwardRepository repository;

    private final BillOrderAwardAssembler assembler;

    @Override
    public int update(BillOrderAwardReq req) {
        return repository.update(assembler.req2VO(req));
    }
}
