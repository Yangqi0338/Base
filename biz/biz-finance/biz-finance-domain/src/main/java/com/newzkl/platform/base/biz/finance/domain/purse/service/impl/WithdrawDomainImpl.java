package com.newzkl.platform.base.biz.finance.domain.purse.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.WithdrawRepository;
import com.newzkl.platform.base.biz.finance.domain.purse.service.WithdrawDomain;
import com.newzkl.platform.base.biz.finance.model.assembler.AccountPurseRollOutAssembler;
import com.newzkl.platform.base.biz.finance.model.purse.req.*;
import com.newzkl.platform.base.biz.finance.model.purse.vo.*;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author niu
 * @description:
 * @date 2023/12/23 13:58
 */
@Service
@RequiredArgsConstructor
public class WithdrawDomainImpl implements WithdrawDomain {

    private final WithdrawRepository withdrawRepository;
    private final AccountPurseRollOutAssembler rollOutAssembler;

    @Override
    public void rollOutApply(RollOutApplyReq req) {
        RollOutApplyVO rollOutApplyVO = rollOutAssembler.req2VO(req);
        rollOutApplyVO.setAccountId(SecurityUtils.getAccountId());
        rollOutApplyVO.setAccountName(SecurityUtils.getUsername());
        withdrawRepository.rollOutApply(rollOutApplyVO);
    }

    @Override
    public boolean rollOutApplyAudit(RollOutApplyAuditReq req) {
        return withdrawRepository.rollOutApplyAudit(req);
    }

    @Override
    public Page<RollOutApplyVO> queryRollOutApplyPage(RollOutApplyQuery req) {
        return withdrawRepository.queryRollOutApplyPage(req);
    }

    @Override
    public RollOutApplyVO rollOutApplyDetail(Long id) {
        return withdrawRepository.rollOutApplyDetail(id);
    }

    @Override
    public void alterRollOutApplyId(Long id) {
        withdrawRepository.alterRollOutApplyId(id);
    }

    @Override
    public boolean alterRollOutTripartiteState(Long applyId, Integer tripartiteState, String tripartiteTradeNo) {
        return withdrawRepository.alterRollOutTripartiteState(applyId, tripartiteState, tripartiteTradeNo);
    }

    @Override
    public boolean alterWithdrawState(AlterWithdrawStateReq req) {
        return withdrawRepository.alterWithdrawState(req);
    }

    @Override
    public WithdrawRecordVO queryTripartiteWithdrawRecord(Long id) {
        return withdrawRepository.queryTripartiteWithdrawRecord(id);
    }

    @Override
    public Page<WithdrawRecordVO> queryTripartiteWithdrawRecordList(TripartiteWithdrawRecordQuery req) {
        return withdrawRepository.queryTripartiteWithdrawRecordList(req);
    }

    @Override
    public List<RollOutApplyVO> queryWithdrawRecords(RollOutApplyQuery req) {
        return withdrawRepository.queryWithdrawRecords(req);
    }

    @Override
    public WithdrawAmountVO queryWithdrawAmount(RoleEnum.CompanyRole role, Long accountId) {
        RollOutApplyQuery req = new RollOutApplyQuery();

        return null;
    }

    @Override
    public List<RollOutApplyExportVO> withdrawRecordsExport(RollOutApplyQuery req) {
        List<RollOutApplyVO> rollOutApplyList = queryRollOutApplyPage(req).getRecords();
        List<RollOutApplyExportVO> exportResponses = rollOutApplyList.stream().map(c -> {
            RollOutApplyExportVO v = TransferUtils.transfer(c, RollOutApplyExportVO::new);
            v.setAuditState(c.getAuditState().getValue());
            v.setApplyAmount(c.getApplyAmount().getAmount().toPlainString());
            v.setHandlingFee(c.getHandlingFee().getAmount().toPlainString());
            v.setArrivalAmount(c.getApplyAmount().subtract(c.getHandlingFee()).getAmount().toPlainString());
            return v;
        }).collect(Collectors.toList());
        return exportResponses;
    }
}
