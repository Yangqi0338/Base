package com.newzkl.platform.base.biz.finance.application.purse.service;


import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuRollOutRes;
import com.newzkl.platform.base.biz.finance.model.person.res.WithdrawNotifyRes;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountWithdrawReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyAuditReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyReq;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;

/**
 * 提现业务编排接口。
 *
 * @author niu
 */
public interface WithdrawService {

    /**
     * 转出申请。
     *
     * @param req 转出申请
     */
    void rollOutApply(RollOutApplyReq req);

    /**
     * 转出申请审核。
     *
     * @param req 审核请求
     * @return 汇付转出结果
     */
    HuiFuRollOutRes rollOutApplyAudit(RollOutApplyAuditReq req);

    /**
     * 客户三方账户提现。
     *
     * @param req 提现请求
     * @return 处理结果
     */
    ScmResult<Object> accountTripartiteWithdraw(AccountWithdrawReq req);

    /**
     * 转出申请三方到账结果回写 (连连转出回调)。
     *
     * <p>迁移自 new-scm {@code IWithdrawAction#alterRollOutTripartiteState}。</p>
     *
     * @param applyId           转出申请ID
     * @param tripartiteState   三方到账状态, 1 = 成功 (成功时补记三方账户余额)
     * @param tripartiteTradeNo 三方交易单号
     */
    void alterRollOutTripartiteState(Long applyId, Integer tripartiteState, String tripartiteTradeNo);

    /**
     * 客户三方账户提现结果回调处理。
     *
     * <p>迁移自 new-scm {@code IWithdrawAction#withdrawNotify}。</p>
     *
     * @param notify 连连提现回调报文
     */
    void withdrawNotify(WithdrawNotifyRes notify);
}
