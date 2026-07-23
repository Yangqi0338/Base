package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountContributeRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.EarningContributeDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.EarningContributeDO;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AccountContributeQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AlterAccountContributeDataReq;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.AccountContributeVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2024/1/25 10:58
 */
@Repository
@RequiredArgsConstructor
public class AccountContributeRepositoryImpl implements AccountContributeRepository {

    private final EarningContributeDAO earningContributeDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init(AccountContributeVO req) {
        // 查看db是否存在
        AccountContributeQuery query = new AccountContributeQuery();
        query.setMyAccountId(req.getAccountId());
        query.setAccountType(req.getAccountType());
        LambdaQueryWrapper<EarningContributeDO> queryWrapper = earningContributeDAO.getLw(query);

        if (earningContributeDAO.exists(queryWrapper)) {
            EarningContributeDO earningContribute = TransferUtils.transfer(req, EarningContributeDO::new);
            earningContributeDAO.insert(earningContribute);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterAccountContribute(List<AlterAccountContributeDataReq> req) {
        req.forEach(earningContributeDAO::alterAccountContribute);
    }

    @Override
    public List<AccountContributeVO> queryAccountContribute(AccountContributeQuery query) {
        LambdaQueryWrapper<EarningContributeDO> queryWrapper = earningContributeDAO.getLw(query);
        Page<EarningContributeDO> pageList = earningContributeDAO.selectPage(RepositorySupport.page(query), queryWrapper);
        return TransferUtils.transfers(pageList.getRecords(), AccountContributeVO.class);
    }
}
