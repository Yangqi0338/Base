package com.newzkl.platform.base.biz.finance.domain.purse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountPurseRepository;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.model.assembler.AccountPurseAlterRecordAssembler;
import com.newzkl.platform.base.biz.finance.model.purse.req.*;
import com.newzkl.platform.base.biz.finance.model.purse.res.BatchQueryAccountPurseRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.TotalSupplierSettleDataRes;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
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
            req.setAccountType(PurseEnum.User.getByRole(SecurityUtils.getIdentity()));
        }
        return accountPurseRepository.queryAccountPurse(req);
    }

    @Override
    public AccountPurseVO queryAccountPurchasePurse(AccountPurseQuery req) {
        if (req.getAccountType() == null) {
            req.setAccountType(PurseEnum.User.getByRole(SecurityUtils.getIdentity()));
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
     * <p>🔴 2026-07-30 修资金安全缺陷: 原实现 {@code new AccountPurseQuery()} 建出空对象后
     * **从未从 {@code req} 填** accountId / accountType / purseType 就传给仓储。而
     * {@code AccountPurseDAO#getLw} 三个条件全是 {@code notEmptyIn/notEmptyEq}(空值跳过),
     * 拼出的 {@code LambdaUpdateWrapper} 无任何 WHERE 约束 →
     * {@code UPDATE account_purse SET earnings = earnings ± ?} **全表动账**。
     * 现按 req 填三个作用域字段, 并加空作用域断言兜底
     *
     * @param relateAward      是否关联账户流水
     * @param earningAlterType 加还是减
     * @param reqs             动账明细, 每条必带 accountId / accountType / purseType
     * @return 恒 true (失败走异常)
     * @throws IllegalArgumentException 任一明细缺 accountId / accountType / purseType 时抛出,
     *                                  防止退化为无 WHERE 的全表 UPDATE
     */
    public boolean doPurseAmount(boolean relateAward, EarningsEnum.PurseAlterTypeEnum earningAlterType, AccountPurseAlterRecordReq[] reqs) {
        if (ArrayUtil.isEmpty(reqs)) return true;

        List<AccountPurseAlterRecordVO> recordVOList = new ArrayList<>();
        for (AccountPurseAlterRecordReq req : reqs) {
            PurseEnum.Type purseType = req.getPurseType();
            // 动账作用域三要素缺一不可: 任一为空都会让 getLw 少拼一个 WHERE 条件, 扩大动账范围
            if (req.getAccountId() == null || req.getAccountType() == null || purseType == null) {
                throw new IllegalArgumentException(
                        "动账作用域不完整, 拒绝执行: accountId=" + req.getAccountId()
                                + ", accountType=" + req.getAccountType() + ", purseType=" + purseType);
            }
            AccountPurseQuery query = new AccountPurseQuery();
            query.setAccountId(req.getAccountId());
            query.setAccountType(req.getAccountType());
            query.setPurseType(purseType);

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
    public Page<AccountPurseAlterRecordVO> queryAccountPurseAlterRecords(AccountPurseAlterRecordQuery req) {
        return accountPurseRepository.queryAccountPurseAlterRecords(req);
    }

    @Override
    public Page<AccountPurseAlterRecordVO> queryChannelRollOutRecords(AccountPurseAlterRecordQuery req) {
        return accountPurseRepository.queryChannelRollOutRecords(req);
    }

    @Override
    public AccountPurseAlterRecordVO queryMaxAmount(AccountPurseAlterRecordQuery req) {
        req.addDescSortField("amount");
        req.resetQuerySingle();

        // 只取首条, 分页壳里的 total 用不上
        return CollUtil.getFirst(queryAccountPurseAlterRecords(req).getRecords());
    }

    @Override
    public TotalSupplierSettleDataRes querySupplierSettleData() {
        return accountPurseRepository.querySupplierSettleData();
    }
}
