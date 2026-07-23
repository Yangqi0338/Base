package com.newzkl.platform.base.biz.finance.domain.purse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountPurseRepository;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.model.assembler.AccountPurseAlterRecordAssembler;
import com.newzkl.platform.base.biz.finance.model.purse.req.*;
import com.newzkl.platform.base.biz.finance.model.purse.res.BatchQueryAccountPurseRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.TotalSupplierSettleDataRes;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseVO;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author niu
 * @description: 客户账户服务接口实现
 * @date 2023/12/18 15:27
 */
@RequiredArgsConstructor
@Service
public class AccountPurseDomainImpl implements AccountPurseDomain {

    private final AccountPurseRepository accountPurseRepository;
    private final AccountPurseAlterRecordAssembler accountPurseAlterRecordAssembler;

    @Override
    public void addAccountPurse(List<AddAccountPurseReq> req) {
        accountPurseRepository.addAccountPurse(req);
    }

    @Override
    public List<AccountPurseVO> queryAccountPurse(AccountPurseQuery req) {
        if (req.getAccountType() == null) {
            req.setAccountType(PurseEnum.FinanceUser.getByRole(SecurityUtils.getRoleId()));
        }
        return accountPurseRepository.queryAccountPurse(req);
    }

    @Override
    public AccountPurseVO queryAccountPurchasePurse(AccountPurseQuery req) {
        if (req.getAccountType() == null) {
            req.setAccountType(PurseEnum.FinanceUser.getByRole(SecurityUtils.getRoleId()));
        }

        AccountPurseVO accountPurseVO = accountPurseRepository.queryAccountPurchasePurse(req);
        if (accountPurseVO == null) {
            accountPurseVO = new AccountPurseVO();
            accountPurseVO.setAccountId(SecurityUtils.getAccountId());
            accountPurseVO.setAccountName(SecurityUtils.getUsername());
        }
        return accountPurseVO;
    }

    @Override
    public List<BatchQueryAccountPurseRes> batchQueryAccountEarning(BatchAccountPurseQuery req) {
        return accountPurseRepository.batchQueryAccountEarning(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addAmount(AccountPurseAlterRecordReq... reqs) {
        return doPurseAmount(true, EarningsEnum.PurseAlterTypeEnum.IN, reqs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refundAddAmount(AccountPurseAlterRecordReq... reqs) {
        return doPurseAmount(false, EarningsEnum.PurseAlterTypeEnum.IN, reqs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean subAmount(AccountPurseAlterRecordReq... reqs) {
        return doPurseAmount(false, EarningsEnum.PurseAlterTypeEnum.OUT, reqs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rollbackAmount(AccountPurseAlterRecordReq... reqs) {
        // TODO 检查是否可以回滚
        return doPurseAmount(true, EarningsEnum.PurseAlterTypeEnum.OUT, reqs);
    }

    /**
     * 修改账户余额
     *
     * @param relateAward      是否关联账户流水
     * @param earningAlterType 加还是减
     */
    public boolean doPurseAmount(boolean relateAward, EarningsEnum.PurseAlterTypeEnum earningAlterType, AccountPurseAlterRecordReq[] reqs) {
        if (ArrayUtil.isEmpty(reqs)) return true;

        List<AccountPurseAlterRecordVO> recordVOList = new ArrayList<>();
        for (AccountPurseAlterRecordReq req : reqs) {
            AccountPurseQuery query = new AccountPurseQuery();
            PurseEnum.PurseType purseType = req.getPurseType();

            if (earningAlterType == EarningsEnum.PurseAlterTypeEnum.IN) {
                accountPurseRepository.addAccountPurseAmount(query, req.getAmount(), relateAward, purseType.isTotalRelation());
            } else if (earningAlterType == EarningsEnum.PurseAlterTypeEnum.OUT) {
                accountPurseRepository.subAccountPurseAmount(query, req.getAmount(), relateAward, purseType.isTotalRelation(), purseType.isNegative());
            }

            AccountPurseAlterRecordVO recordVO = accountPurseAlterRecordAssembler.req2VO(req);
            recordVO.setEarningAlterType(earningAlterType);
            recordVOList.add(recordVO);
        }
        saveAccountPurseAlterRecord(recordVOList);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAccountPurseAlterRecord(List<AccountPurseAlterRecordVO> accountPurseAlterRecords) {
        accountPurseAlterRecords.forEach(record -> {
            if (StrUtil.isBlank(record.getRemark())) {
                record.setRemark(record.getAlterType().getInfo());
            }
        });
        accountPurseRepository.saveAccountPurseAlterRecord(accountPurseAlterRecords);
    }

    @Override
    public List<AccountPurseAlterRecordVO> queryAccountPurseAlterRecords(AccountPurseAlterRecordQuery req) {
        return accountPurseRepository.queryAccountPurseAlterRecords(req);
    }

    @Override
    public AccountPurseAlterRecordVO queryMaxAmount(AccountPurseAlterRecordQuery req) {
        req.addDescSortField("amount");
        req.resetQuerySingle();

        List<AccountPurseAlterRecordVO> recordPage = queryAccountPurseAlterRecords(req);
        return CollUtil.getFirst(recordPage);
    }

    @Override
    public TotalSupplierSettleDataRes querySupplierSettleData() {
        return accountPurseRepository.querySupplierSettleData();
    }
}
