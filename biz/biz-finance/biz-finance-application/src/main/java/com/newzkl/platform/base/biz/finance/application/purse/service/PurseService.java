package com.newzkl.platform.base.biz.finance.application.purse.service;


import com.newzkl.platform.base.biz.finance.model.purse.req.AmountDistributionReq;
import com.newzkl.platform.base.common.ddd.model.ScmResult;

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
    ScmResult<Object> platformToOperator(AmountDistributionReq req);

    /**
     * 运营商给渠道商分配采购金。
     *
     * @param req 分配请求
     * @return 处理结果
     */
    ScmResult<Object> operatorToChannel(AmountDistributionReq req);

    /**
     * 渠道商自同步采购金。
     *
     * @param req 分配请求
     * @return 处理结果
     */
    ScmResult<Object> channelBalanceSync(AmountDistributionReq req);
}
