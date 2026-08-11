package com.newzkl.platform.base.biz.finance.application.earnings.service;


import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningsExecReq;

/**
 * 分润查询接口
 *
 * @author niu
 */
public interface EarningService {

    /**
     * 去分润
     * @param req
     */
    void doEarning(EarningsExecReq req);

}
