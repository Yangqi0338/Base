package com.newzkl.platform.base.biz.finance.application.purse.service;


import com.newzkl.platform.base.biz.finance.model.purse.req.AmountDistributionReq;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;

/**
 * 采购金分配编排接口 (平台线下)
 *
 * @author niu
 */
public interface PurseService {

    /**
     * 渠道商自同步采购金
     *
     * @param req 分配请求
     * @return 处理结果
     */
    PlatformResult<Object> channelBalanceSync(AmountDistributionReq req);
}
