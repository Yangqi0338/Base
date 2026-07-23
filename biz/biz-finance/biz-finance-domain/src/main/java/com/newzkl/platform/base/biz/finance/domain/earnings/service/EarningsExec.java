package com.newzkl.platform.base.biz.finance.domain.earnings.service;


import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningsExecReq;

import java.util.List;

/**
 * @author niu
 * @description: 执行分润
 * @date 2023/12/18 16:36
 */
public interface EarningsExec {


    /**
     * 消费分润
     *
     * @param req
     * @return 已完成分润的数据
     */
    List<EarningRecordReq> consumeEarnings(EarningsExecReq req);
}
