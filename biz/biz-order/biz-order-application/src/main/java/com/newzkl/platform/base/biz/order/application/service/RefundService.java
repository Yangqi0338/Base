package com.newzkl.platform.base.biz.order.application.service;


import com.newzkl.platform.base.biz.order.model.req.RefundCommand;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/811:16
 */
public interface RefundService {
    /**
     * 售后单创建
     * @param refundCommand
     * @return
     */
    Long refundCreateApi(RefundCommand refundCommand);

    /**
     * 售后单创建
     * @param refundCommand
     * @return
     */
    Long refundCreate(RefundCommand refundCommand);

    /**
     * 售后审核(渠道商/供应商统一入口, 按身份分流)
     *
     * @param identity 审核方身份(定时任务无登录态, 显式传入; web 端点由 token 取)
     * @param refundId 售后单id
     * @param orderNo  交易单号(与 refundId 二选一)
     * @param execute  审核操作 0 拒绝 1 通过
     * @param reason   拒绝原因
     * @param isAudit  是否系统超时自动审核
     */
    void audit(AccountEnum.Identity identity, Long refundId, String orderNo, CommonEnum.YesOrNo execute, String reason, boolean isAudit);

    void supplierConfirmRefundFreight(Long refundId);
}
