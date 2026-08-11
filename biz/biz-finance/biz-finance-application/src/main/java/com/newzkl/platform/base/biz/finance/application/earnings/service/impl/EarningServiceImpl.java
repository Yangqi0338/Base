package com.newzkl.platform.base.biz.finance.application.earnings.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.newzkl.platform.base.biz.finance.application.earnings.service.EarningService;
import com.newzkl.platform.base.biz.finance.application.purse.service.PurseService;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.EarningsExec;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningsExecReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordReq;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 分润记录查询实现
 *
 * @author niu
 */
@Service
@RequiredArgsConstructor
public class EarningServiceImpl implements EarningService {

    private final EarningsExec earningsExec;
    private final AccountPurseDomain accountPurseDomain;

    @Override
    public void doEarning(EarningsExecReq req) {
        // 1、执行分润流程
        List<EarningRecordReq> earningInfos = earningsExec.consumeEarnings(req);
        // 2、增加客户账户余额
        if (earningInfos.isEmpty() || req.getConsumeType().equals(EarningsEnum.ConsumeType.GOODS)) {
            return;
        }
        List<AccountPurseAlterRecordReq> amountReqList = earningInfos.stream().map(x -> {
            AccountPurseAlterRecordReq recordReq = TransferUtils.transfer(x, AccountPurseAlterRecordReq.class);
            recordReq.setAccountType(x.getEarningType().getUser());
            recordReq.setJoinRecordId(x.getJoinOrderNo());
            return recordReq;
        }).toList();
        // 3、保存账户变动记录
        accountPurseDomain.addAmount(ArrayUtil.toArray(amountReqList, AccountPurseAlterRecordReq.class));
    }

}
