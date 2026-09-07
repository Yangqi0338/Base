package com.newzkl.platform.base.biz.finance.domain.purse.service.impl;


import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountPurseRepository;
import com.newzkl.platform.base.biz.finance.domain.purse.service.TripartitePurseDomain;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author niu
 * @description:
 * @date 2023/12/20 14:32
 */
@Service
@RequiredArgsConstructor
public class TripartitePurseDomainImpl implements TripartitePurseDomain {

    private final AccountPurseRepository accountPurseRepository;

    @Override
    public void addAccountTripartitePurse(AccountTripartitePurseVO accountTripartitePurse) {
        accountPurseRepository.addAccountTripartitePurse(accountTripartitePurse);
    }
}
