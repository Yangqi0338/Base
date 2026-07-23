package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.AccountJobRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.AccountJobDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.AccountJobDO;
import com.newzkl.platform.base.biz.account.model.req.AccountJobQuery;
import com.newzkl.platform.base.biz.account.model.vo.AccountJobVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台角色
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class AccountJobRepositoryImpl implements AccountJobRepository {

    private final AccountJobDAO accountJobDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(AccountJobVO accountJob) {
        AccountJobDO accountJobDO = TransferUtils.transfer(accountJob, AccountJobDO::new);
        accountJobDAO.insertOrUpdate(accountJobDO);
        return accountJobDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        accountJobDAO.deleteByIds(idList);
    }

    @Override
    public AccountJobVO detail(Long id) {
        AccountJobDO accountJobDO = accountJobDAO.selectById(id);
        return TransferUtils.transfer(accountJobDO, AccountJobVO::new);
    }

    @Override
    public Page<AccountJobVO> pageList(AccountJobQuery accountJobQuery) {
        Page<AccountJobDO> pageList = accountJobDAO.selectPage(RepositorySupport.page(accountJobQuery),
                accountJobDAO.getLw(accountJobQuery));

        return TransferUtils.transferPage(pageList, AccountJobVO::new);
    }

}
