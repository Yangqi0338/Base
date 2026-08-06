package com.newzkl.platform.base.biz.finance.application.purse.service;


import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuRollOutRes;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountWithdrawReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyAuditReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyReq;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;

/**
 * 提现业务编排接口
 *
 * @author niu
 */
public interface WithdrawService {

    /**
     * 转出申请
     *
     * @param req 转出申请
     */
    void rollOutApply(RollOutApplyReq req);

    /**
     * 转出申请审核
     *
     * @param req 审核请求
     * @return 汇付转出结果
     */
    HuiFuRollOutRes rollOutApplyAudit(RollOutApplyAuditReq req);

    /**
     * 客户三方账户提现
     *
     * @param req 提现请求
     * @return 处理结果
     */
    PlatformResult<Boolean> accountTripartiteWithdraw(AccountWithdrawReq req);
}
