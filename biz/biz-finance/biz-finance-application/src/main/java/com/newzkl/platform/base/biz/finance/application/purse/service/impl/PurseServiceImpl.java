package com.newzkl.platform.base.biz.finance.application.purse.service.impl;


import com.newzkl.platform.base.biz.finance.application.purse.service.PurseService;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.AmountDistributionReq;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.ddd.model.constant.FinanceErrorCode;
import com.newzkl.platform.base.biz.finance.model.support.api.UpIdRes;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 采购金分配编排实现 (平台线下)
 *
 * @author niu
 */
@Service
@RequiredArgsConstructor
public class PurseServiceImpl implements PurseService {

    private final AccountPurseDomain accountPurseService;

    private final AccountPurseConfigDomain accountPurseConfigDomain;

    private final AccountApi accountFacade;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlatformResult<Object> channelBalanceSync(AmountDistributionReq req) {
        Long accountId = req.getAccountId();
        Money amount = req.getAmount();
        try {
            // 增加收益账户余额
            AccountPurseAlterRecordReq channelRecordReq = buildAccountPurseAlterRecord(req, PurseEnum.PurseAlterType.CHANNEL_SYNC_DOWNSTREAM,
                    PurseEnum.FinanceUser.CHANNEL, amount, PurseEnum.PurseType.PURCHASE);
            accountPurseService.addAmount(channelRecordReq);
            // 渠道商采购金充值后，更新服务费 (阈值比较按分, Money → int 分)
            accountPurseConfigDomain.alterChannelNowChargeConfig(accountId, (int) amount.getCent());
        } catch (Exception e) {
            return PlatformResult.fail(e.getMessage());
        }
        return PlatformResult.success();
    }

    private AccountPurseAlterRecordReq buildAccountPurseAlterRecord(AmountDistributionReq distributionReq,
                                                                    PurseEnum.PurseAlterType alterType,
                                                                    PurseEnum.FinanceUser accountType,
                                                                    Money amount,
                                                                    PurseEnum.PurseType purseType) {
        AccountPurseAlterRecordReq req = new AccountPurseAlterRecordReq();
        req.setAccountId(distributionReq.getAccountId());
        req.setPurseType(purseType);
        req.setAccountType(accountType);
        req.setAlterType(alterType);
        req.setAmount(amount);
        req.setJoinRecordId(distributionReq.getOfflineRecordRemark());
        return req;
    }
}
