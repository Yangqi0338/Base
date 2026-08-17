package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.WithdrawRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.AccountPurseRollOutDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.AccountWithdrawRecordDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.AccountPurseRollOutDO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.AccountWithdrawRecordDO;
import com.newzkl.platform.base.biz.finance.model.purse.req.AlterWithdrawStateReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyAuditReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.TripartiteWithdrawRecordQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.RollOutApplyVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.WithdrawRecordVO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2023/12/23 13:59
 */
@Repository
@RequiredArgsConstructor
public class WithdrawRepositoryImpl implements WithdrawRepository {

    private final AccountPurseRollOutDAO rollOutDAO;

    private final AccountWithdrawRecordDAO accountWithdrawRecordDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollOutApply(RollOutApplyVO req) {
        AccountPurseRollOutDO accountPurseRollOut = TransferUtils.transfer(req, AccountPurseRollOutDO::new);
        rollOutDAO.insert(accountPurseRollOut);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rollOutApplyAudit(RollOutApplyAuditReq req) {
        int count = rollOutDAO.rollOutApplyAudit(req);
        return count == 1;
    }

    @Override
    public Page<RollOutApplyVO> queryRollOutApplyPage(RollOutApplyQuery query) {
        Page<AccountPurseRollOutDO> pageList = rollOutDAO.selectPage(RepositorySupport.page(query), rollOutDAO.getLw(query));
        return TransferUtils.transferPage(pageList, RollOutApplyVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RollOutApplyVO rollOutApplyDetail(Long id) {
        AccountPurseRollOutDO accountPurseRollOut = rollOutDAO.selectForUpdateById(id);
        return TransferUtils.transfer(accountPurseRollOut, RollOutApplyVO::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterRollOutApplyId(Long id) {
        rollOutDAO.alterRollOutApplyId(id, SnowflakeGenerator.getSnowflakeId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean alterRollOutTripartiteState(Long applyId, Integer tripartiteState, String tripartiteTradeNo) {
        return rollOutDAO.alterRollOutTripartiteState(applyId, tripartiteState, tripartiteTradeNo) == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean alterWithdrawState(AlterWithdrawStateReq req) {
        // 状态为0的才允许修改
        // 根据id修改完成时间/三方交易单号|状态
        int affectRows = accountWithdrawRecordDAO.update(new LambdaUpdateWrapper<AccountWithdrawRecordDO>()
                .set(AccountWithdrawRecordDO::getFinishTime, req.getFinishTime())
                .set(AccountWithdrawRecordDO::getTripartiteTradeNo, req.getTripartiteTradeNo())
                .set(AccountWithdrawRecordDO::getState, req.getState())
                .eq(AccountWithdrawRecordDO::getId, req.getId())
                .eq(req.getState() != null && req.getState() != AuditEnum.WithdrawSate.SUCCESS, AccountWithdrawRecordDO::getState, AuditEnum.WithdrawSate.AUDITING)
        );
        return affectRows == 1;
    }

    @Override
    public WithdrawRecordVO queryTripartiteWithdrawRecord(Long id) {
        AccountWithdrawRecordDO accountWithdrawRecordDO = accountWithdrawRecordDAO.selectById(id);
        return TransferUtils.transfer(accountWithdrawRecordDO, WithdrawRecordVO::new);
    }

    @Override
    public Page<WithdrawRecordVO> queryTripartiteWithdrawRecordList(TripartiteWithdrawRecordQuery query) {
        Page<AccountWithdrawRecordDO> pageList = accountWithdrawRecordDAO.selectPage(RepositorySupport.page(query),
                accountWithdrawRecordDAO.getLw(query).orderBy(query)
        );
        return TransferUtils.transferPage(pageList, WithdrawRecordVO.class);
    }

    @Override
    public List<RollOutApplyVO> queryWithdrawRecords(RollOutApplyQuery req) {
        return rollOutDAO.queryWithdrawRecords(req);
    }

}
