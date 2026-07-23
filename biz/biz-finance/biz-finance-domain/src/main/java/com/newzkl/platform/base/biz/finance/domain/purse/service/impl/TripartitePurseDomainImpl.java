package com.newzkl.platform.base.biz.finance.domain.purse.service.impl;


import java.util.List;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountPurseRepository;
import com.newzkl.platform.base.biz.finance.domain.purse.service.TripartitePurseDomain;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountTripartitePurseQuery;
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

    @Override
    public void alterAccountTripartitePurse(AccountTripartitePurseVO accountTripartitePurse) {
        accountPurseRepository.alterAccountTripartitePurse(accountTripartitePurse);
    }

    @Override
    public AccountTripartitePurseVO queryAccountTripartitePurse(Long accountId) {
        return accountPurseRepository.queryAccountTripartitePurse(accountId);
    }

    @Override
    public List<AccountTripartitePurseVO> queryPageAccountTripartitePurse(AccountTripartitePurseQuery query) {
        return accountPurseRepository.queryAccountTripartitePurse(query);
    }

    @Override
    public String queryCommitInfo(Long accountId) {
        return accountPurseRepository.queryCommitInfo(accountId);
    }

    @Override
    public void addAccountTripartitePurseAmount(Long accountId, Integer amount) {
        accountPurseRepository.addAccountTripartitePurseAmount(accountId, amount);
    }

    @Override
    public void subAccountTripartitePurseAmount(Long accountId, Integer amount) {
        accountPurseRepository.subAccountTripartitePurseAmount(accountId, amount);
    }
}
