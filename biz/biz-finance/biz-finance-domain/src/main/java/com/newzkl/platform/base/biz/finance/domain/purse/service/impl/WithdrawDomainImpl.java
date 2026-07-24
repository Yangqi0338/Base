package com.newzkl.platform.base.biz.finance.domain.purse.service.impl;

import com.alibaba.fastjson2.JSON;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.WithdrawRepository;
import com.newzkl.platform.base.biz.finance.domain.purse.service.WithdrawDomain;
import com.newzkl.platform.base.biz.finance.model.assembler.AccountPurseRollOutAssembler;
import com.newzkl.platform.base.biz.finance.model.assembler.AccountWithdrawRecordAssembler;
import com.newzkl.platform.base.biz.finance.model.purse.req.*;
import com.newzkl.platform.base.biz.finance.model.purse.vo.*;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
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
    private final AccountWithdrawRecordAssembler withdrawRecordAssembler;

    @Override
    public ConfigWithdrawVO defaultWithdrawConfig() {
        return withdrawRepository.defaultWithdrawConfig();
    }

    @Override
    public void alterWithdrawConfig(ConfigWithdrawVO req) {
        withdrawRepository.alterWithdrawConfig(req);
    }

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
    public List<RollOutApplyVO> queryRollOutApplyPage(RollOutApplyQuery req) {
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
    public Long saveAccountWithdraw(Long accountId, Integer amount) {
        ConfigWithdrawVO configWithdrawVO = withdrawRepository.defaultWithdrawConfig();
        WithdrawConfig withdraw = configWithdrawVO.getWithdraw();
        Integer realAmount = amount;
        Integer goodsPoints = 0;
        if (withdraw != null) {
            goodsPoints = ScmUtil.percentFloor(realAmount, withdraw.getScoreRatio());
            realAmount = ScmUtil.percentFloor(realAmount, withdraw.getRatio());
        }

        AccountWithdrawRecordReq withdrawRecordReq = new AccountWithdrawRecordReq();
        withdrawRecordReq.setAccountId(accountId);
        withdrawRecordReq.setAmount(realAmount);
        withdrawRecordReq.setGoodsPoints(goodsPoints);
        withdrawRecordReq.setConfig(JSON.toJSONString(withdraw));
        return withdrawRepository.saveAccountWithdraw(withdrawRecordAssembler.req2VO(withdrawRecordReq));
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
    public List<WithdrawRecordVO> queryTripartiteWithdrawRecordList(TripartiteWithdrawRecordQuery req) {
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
        List<RollOutApplyVO> rollOutApplyList = queryRollOutApplyPage(req);
        List<RollOutApplyExportVO> exportResponses = rollOutApplyList.stream().map(c -> {
            RollOutApplyExportVO v = TransferUtils.transfer(c, RollOutApplyExportVO::new);
            v.setAuditState(c.getAuditState().getValue());
            v.setApplyAmount(ScmUtil.toYuanStr(c.getApplyAmount()));
            v.setHandlingFee(ScmUtil.toYuanStr(c.getHandlingFee()));
            v.setArrivalAmount(ScmUtil.toYuanStr(c.getApplyAmount() - c.getHandlingFee()));
            return v;
        }).collect(Collectors.toList());
        return exportResponses;
    }
}
