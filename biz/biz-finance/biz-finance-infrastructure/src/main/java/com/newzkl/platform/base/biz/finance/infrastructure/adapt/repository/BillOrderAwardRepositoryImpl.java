package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.finance.domain.adapt.repository.BillOrderAwardRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.BillOrderAwardDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.BillOrderAwardDO;
import com.newzkl.platform.base.biz.finance.model.account.req.BillOrderAwardQuery;
import com.newzkl.platform.base.biz.finance.model.account.res.BillOrderAwardAmountRes;
import com.newzkl.platform.base.biz.finance.model.account.vo.BillOrderAwardVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BillOrderAwardRepositoryImpl implements BillOrderAwardRepository {

    private final BillOrderAwardDAO billOrderAwardDAO;

    @Override
    public BillOrderAwardAmountRes selectSumAmountByCondition(BillOrderAwardQuery req) {
        return billOrderAwardDAO.selectSumAmountByCondition(billOrderAwardDAO.getLw(req));
    }

    @Override
    public int update(BillOrderAwardVO req) {
        BillOrderAwardDO awardDO = TransferUtils.transfer(req, BillOrderAwardDO::new);
        return billOrderAwardDAO.updateById(awardDO);
    }

    @Override
    public BillOrderAwardVO selectOne(BillOrderAwardQuery query) {
        BillOrderAwardDO awardDO = billOrderAwardDAO.selectOne(billOrderAwardDAO.getLw(query));
        return TransferUtils.transfer(awardDO, BillOrderAwardVO::new);
    }
}
