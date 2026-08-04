package com.newzkl.platform.base.biz.finance.domain.earnings.service;


import com.newzkl.platform.base.biz.finance.model.earnings.req.AlterAccountContributeDataReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningsExecReq;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;

import java.util.List;

/**
 * @author niu
 * @description: 消费分润
 * @date 2023/12/18 17:52
 */
public interface ConsumeEarnings<T extends EarningsExecReq> {

    /**
     * 分润
     * 罗列出需要分润什么, 有什么贡献
     * @param req
     */
    List<EarningRecordReq> earnings(T req, List<AlterAccountContributeDataReq> contributeDataReqs);

    /**
     * 消费类型
     *
     * @return
     */
    EarningsEnum.ConsumeType consumeType();
}
