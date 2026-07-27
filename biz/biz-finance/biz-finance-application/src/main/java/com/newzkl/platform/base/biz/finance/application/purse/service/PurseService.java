package com.newzkl.platform.base.biz.finance.application.purse.service;


import com.newzkl.platform.base.biz.finance.model.purse.req.AmountDistributionReq;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;

/**
 * 采购金分配编排接口 (平台线下)。
 *
 * @author niu
 */
public interface PurseService {

    /**
     * 平台给运营商分配采购金。
     *
     * @param req 分配请求
     * @return 处理结果
     */
    PlatformResult<Boolean> platformToOperator(AmountDistributionReq req);

    /**
     * 运营商给渠道商分配采购金。
     *
     * @param req 分配请求
     * @return 处理结果
     */
    PlatformResult<Boolean> operatorToChannel(AmountDistributionReq req);

    /**
     * 渠道商自同步采购金。
     *
     * @param req 分配请求
     * @return 处理结果
     */
    PlatformResult<Object> channelBalanceSync(AmountDistributionReq req);
}
