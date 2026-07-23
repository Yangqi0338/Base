package com.newzkl.platform.base.biz.finance.domain.earnings.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.EarningStrategySupport;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.ConsumeEarnings;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.EarningsExec;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AlterAccountContributeDataReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningsExecReq;
import com.newzkl.platform.base.biz.finance.model.event.SkuOrderWaitEarningVO;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author niu
 * @description: 分润实现
 * @date 2023/12/18 17:42
 */
@Service
public class EarningsExecImpl extends EarningStrategySupport implements EarningsExec {

    @Override
    public List<EarningRecordReq> consumeEarnings(EarningsExecReq req) {
        // 1、获取消费类型对应的分润逻辑实现对象
        ConsumeEarnings consumeEarnings = EarningStrategySupport.consumeTypeMap.get(req.getConsumeType());
        // 2、执行分润逻辑
        List<AlterAccountContributeDataReq> contributeDataReqs = new ArrayList<>();
        List<EarningRecordReq> earningRecordReqs = consumeEarnings.earnings(req, contributeDataReqs);

        // 移除待结算的分润记录
        List<EarningRecordReq> settleEarningInfos = CollUtil.removeWithAddIf(earningRecordReqs,
                it -> it.getState() == EarningsEnum.State.SETTLE);

        // 3、将分润结果入库保存
        if (CollUtil.isNotEmpty(earningRecordReqs)) {
            consumeEarningDataRepository.saveEarningRecord(recordAssembler.req2VO(earningRecordReqs));
        }

        // 4、将待结算的分润结果入库保存
        if (CollUtil.isNotEmpty(settleEarningInfos)) {
            // 预估收益发送mq消息，等待分润时间节点条件满足后执行增加余额、更新贡献等
            consumeEarningDataRepository.sendMessage(new SkuOrderWaitEarningVO(
                    req.getId(), req.getConsumeType(),
                    contributeDataReqs, settleEarningInfos
            ));
        }
        return earningRecordReqs;
    }

}
