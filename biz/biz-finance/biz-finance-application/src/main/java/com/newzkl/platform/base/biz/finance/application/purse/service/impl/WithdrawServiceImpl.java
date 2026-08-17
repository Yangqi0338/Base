package com.newzkl.platform.base.biz.finance.application.purse.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.application.purse.service.WithdrawService;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountPurseConfigRepository;
import com.newzkl.platform.base.biz.finance.domain.hf.HuiFuMethod;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.TripartitePurseDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.WithdrawDomain;
import com.newzkl.platform.base.biz.finance.model.pay.req.huifu.HuiFuRollOutReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuRollOutRes;
import com.newzkl.platform.base.biz.finance.model.pay.vo.CommitInfoExt;
import com.newzkl.platform.base.biz.finance.model.purse.req.*;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.RollOutApplyVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.constant.FinanceErrorCode;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 提现业务编排实现
 *
 * @author niu
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {

    private final WithdrawDomain withdrawDomain;
    private final AccountPurseDomain accountPurseService;
    private final AccountPurseConfigRepository accountPurseConfigRepository;
    private final TripartitePurseDomain tripartitePurse;
    private final AccountApi accountApi;

    private static HuiFuRollOutReq buildRollOutReq(RollOutApplyVO rollOutApplyVO) {
        HuiFuRollOutReq rollOutReq = new HuiFuRollOutReq();
        // HuiFu 边界: Money → 分 Integer
        rollOutReq.setApplyAmount((int) rollOutApplyVO.getApplyAmount().getCent());
        rollOutReq.setHuifuId(rollOutApplyVO.getTripartiteAccountId());
        rollOutReq.setRollOutId(rollOutApplyVO.getId());
        return rollOutReq;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HuiFuRollOutRes rollOutApplyAudit(RollOutApplyAuditReq req) {
        // 1、保存审核结果
        if (!withdrawDomain.rollOutApplyAudit(req)) {
            return new HuiFuRollOutRes();
        }
        RollOutApplyVO rollOutApplyVO = withdrawDomain.rollOutApplyDetail(req.getRollOutApplyId());
        // 2、审核失败后，退回收益余额
        if (req.getAuditSate() == 2) {
            AccountPurseAlterRecordReq recordReq = buildAccountPurseAlterRecord(rollOutApplyVO);
            accountPurseService.refundAddAmount(recordReq);
            return new HuiFuRollOutRes();
        }
        // 3、修改账户变动记录remark为审核结果 TODO

        //4、提现
        HuiFuRollOutReq rollOutReq = buildRollOutReq(rollOutApplyVO);
        return HuiFuMethod.rollOut(rollOutReq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollOutApply(RollOutApplyReq req) {
        // 获取渠道商提现配置
        ChannelConfigVO channelConfigVO = accountPurseConfigRepository.defaultChannelConfig();
        // 检查提现申请
        checkRollOutApply(req, channelConfigVO);

        AccountTripartitePurseVO accountTripartitePurseVO = tripartitePurse.queryAccountTripartitePurse(SecurityUtils.getAccountId());
        if (accountTripartitePurseVO == null || PurseEnum.TripartitePurchaseStatus.NORMAL != accountTripartitePurseVO.getUserStatus()) {
            throw new PlatformException(FinanceErrorCode.NOT_OPEN_ACCOUNT);
        }
        if (!req.getAmount().greaterThanZero()) {
            throw new PlatformException(FinanceErrorCode.NOT_OPEN_ACCOUNT);
        }
        // 根据角色code获取客户类型
        PurseEnum.FinanceUser accountType = req.getAccountType();
        if (accountType == PurseEnum.FinanceUser.SUPPLIER) {
            // limitAmount 为分 Integer 门槛
            Integer restrict = accountApi.limitAmount(SecurityUtils.getAccountId());
            if (restrict > 0 && req.getAmount().smallerThan(Money.of(restrict))) {
                return;
            }
        }

        // 1、扣减收益余额
        AccountPurseAlterRecordReq recordReq = buildAccountPurseAlterRecord(req);
        boolean flag = accountPurseService.subAmount(recordReq);
        // false为余额不足
        if (flag) {
            // 2、保存申请
            req.setAccountType(accountType);
            CommitInfoExt infoExt = JSONUtil.toBean(accountTripartitePurseVO.getCommitInfo(), CommitInfoExt.class);
            if (infoExt == null || StrUtil.isBlank(infoExt.getHuifuId())) {
                throw new PlatformException(FinanceErrorCode.NOT_OPEN_ACCOUNT);
            }
            req.setTripartiteAccountId(infoExt.getHuifuId());
            withdrawDomain.rollOutApply(req);
        }
    }

    private AccountPurseAlterRecordReq buildAccountPurseAlterRecord(RollOutApplyReq rollOutApplyVO) {
        AccountPurseAlterRecordReq req = new AccountPurseAlterRecordReq();
        req.setAccountId(SecurityUtils.getAccountId());
        req.setPurseType(rollOutApplyVO.getPurseType());
        req.setAccountType(rollOutApplyVO.getAccountType());
        req.setAlterType(PurseEnum.PurseAlterType.ROLL_OUT);
        req.setAmount(rollOutApplyVO.getAmount());
        return req;
    }

    private AccountPurseAlterRecordReq buildAccountPurseAlterRecord(RollOutApplyVO rollOutApplyVO) {
        AccountPurseAlterRecordReq req = new AccountPurseAlterRecordReq();
        req.setAccountId(rollOutApplyVO.getAccountId());
        req.setPurseType(rollOutApplyVO.getPurseType());
        req.setAccountType(rollOutApplyVO.getAccountType());
        req.setAlterType(PurseEnum.PurseAlterType.ROLL_OUT_REFUSE);
        req.setAmount(rollOutApplyVO.getApplyAmount());
        req.setJoinRecordId(rollOutApplyVO.getId());
        return req;
    }

    private void checkRollOutApply(RollOutApplyReq req, ChannelConfigVO channelConfigVO) {
        // 判断是否为渠道商且提现实为总余额
        if (req.getAccountType() == PurseEnum.FinanceUser.CHANNEL &&
                req.getPurseType() == PurseEnum.PurseType.GOODS_INCOME) {
            //校验最小提现金额
            if (channelConfigVO.getMinimumWithdrawalAmount().greaterThan(req.getAmount())) {
                throw new PlatformException(FinanceErrorCode.LESS_THAN_MINIMUM_WITHDRAWAL_AMOUNT);
            }

            //查询当日提交的审核中或已通过的转出申请
            RollOutApplyQuery rollOutApplyReq = TransferUtils.transfer(req, RollOutApplyQuery::new);
            rollOutApplyReq.setAccountId(SecurityUtils.getAccountId());
            rollOutApplyReq.setApplyTimeL(DateUtil.beginOfDay(DateUtil.date()).toLocalDateTime());
            rollOutApplyReq.setApplyTimeR(DateUtil.endOfDay(DateUtil.date()).toLocalDateTime());
            rollOutApplyReq.setAuditStateList(Arrays.asList(AuditEnum.WithdrawSate.AUDITING, AuditEnum.WithdrawSate.SUCCESS));
            List<RollOutApplyVO> rollOutApplyVOS = withdrawDomain.queryWithdrawRecords(rollOutApplyReq);
            //单日申请提现总金额 (分累加)
            long countApplyAmount = rollOutApplyVOS.stream()
                    .mapToLong(v -> v.getApplyAmount().getCent())
                    .sum();
            //校验单日提现最高金额
            if (channelConfigVO.getMaximumDailyWithdrawalAmount().getCent() < countApplyAmount) {
                throw new PlatformException(FinanceErrorCode.GREATER_THAN_MAXIMUM_DAILY_WITHDRAWAL_AMOUNT);
            }

            //填充手续费,提现手续费比例单位为千分制整数，所以需要除以10得出单位为分的手续费
            req.setHandlingFee(req.getAmount().multiply(channelConfigVO.getWithdrawalFee()).divide(10L));
        }
    }

    @Override
    public PlatformResult<Boolean> accountTripartiteWithdraw(AccountWithdrawReq req) {
        return null;
    }

}
