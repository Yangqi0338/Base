package com.newzkl.platform.base.biz.finance.application.earnings.service.consume;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.EarningStrategySupport;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.ConsumeEarnings;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AlterAccountContributeDataReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningsAwardExecListReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningsAwardExecReq;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.AwardInfoVO;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动分润 (奖励分红) 消费策略实现
 *
 * @author niu
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AwardsEarningsImpl extends EarningStrategySupport implements ConsumeEarnings<EarningsAwardExecListReq> {

    @Override
    public List<EarningRecordReq> earnings(EarningsAwardExecListReq request, List<AlterAccountContributeDataReq> contributeDataReqs) {
        List<EarningRecordReq> earningInfos = new ArrayList<>();

        List<EarningsAwardExecReq> reqList = request.getList();

        for (EarningsAwardExecReq req : reqList) {
            // 1、执行分润流程
            EarningRecordReq earningRecordReq = new EarningRecordReq();
            earningRecordReq.setAmount(req.getAmount());
            earningRecordReq.setEarningType(EarningsEnum.EarningType.AWARD_DIVIDEND);
            earningRecordReq.setAccountId(req.getAccountId());
            earningRecordReq.setAccountName(req.getAccountName());
            earningRecordReq.setJoinOrderNo(req.getDividendId());

            AwardInfoVO awardInfo = new AwardInfoVO();
            awardInfo.setActivityName(req.getActivityName());
            awardInfo.setDividendMethod(req.getDividendMethod());
            awardInfo.setRatio(Double.valueOf(req.getPercent()));
            earningRecordReq.setGoodsInfo(JSONUtil.toJsonStr(awardInfo));

            earningRecordReq.setPurseType(PurseEnum.PurseType.AWARD_INCOME);
            earningRecordReq.setAlterType(PurseEnum.PurseAlterType.BILL_ORDER_AWARD_INCOME);
            earningRecordReq.setState(EarningsEnum.State.FINISH);
            earningInfos.add(earningRecordReq);
        }

        return earningInfos;
    }


    @Override
    public EarningsEnum.ConsumeType consumeType() {
        return EarningsEnum.ConsumeType.DIVIDEND_BONUS;
    }
}
