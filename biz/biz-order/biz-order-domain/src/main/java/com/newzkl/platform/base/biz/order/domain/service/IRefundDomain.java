package com.newzkl.platform.base.biz.order.domain.service;


import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.model.req.ApplyPlatformCommand;
import com.newzkl.platform.base.biz.order.model.req.RefundCommand;
import com.newzkl.platform.base.biz.order.model.res.RefundAuditRes;
import com.newzkl.platform.base.biz.order.model.res.RefundCreateRes;
import com.newzkl.platform.base.biz.order.model.vo.RefundFreightVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderAggVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;

/**
* 售后单
* @author fang
*/
public interface IRefundDomain {

    /**
     * 创建
     * @param refundCommand
     * @param orderAggVO
     * @return
     */
    RefundCreateRes refundCreate(RefundCommand refundCommand, SpuOrderAggVO orderAggVO);
    /**
     * 同意售后 api接口用
     * @param refundId
     * @param role
     * @return
     */
    RefundAuditRes agreeAudit(Long refundId, RoleEnum.CompanyRole role);
    /**
     * 拒绝售后 api接口用
     * @param refundId
     * @param role
     * @param reason
     * @return
     */
    RefundAuditRes refuseAudit(Long refundId, RoleEnum.CompanyRole role, String reason);
    /**
     * 同意售后 给平台用
     * @param refundId
     * @param role
     * @return
     */
    RefundAuditRes agreeAuditV2(Long refundId, RoleEnum.CompanyRole role);
    /**
     * 确认收货
     * @param refundId
     */
    RefundAuditRes confirmRefundFreight(Long refundId);
    /**
     * 提交退货物流
     * @param refundFreightVO
     */
    void submitRefundFreight(RefundFreightVO refundFreightVO);
    /**
     * 拒绝收货
     * @param refundId
     */
    void refuseRefundFreight(Long refundId);
    /**
     * 终止售后
     * @param role 角色
     * @param accountId 用户ID
     * @param refundId 售后ID
     */
    void stopAudit(RoleEnum.CompanyRole role, Long accountId, Long refundId);
    /**
     * 售后已打款通知
     * @param refundId
     * @param channelId
     */
    void sellAfterRefundNotify(Long refundId, Long channelId, String outRefundId);
    /**
     * 获取外部订单售后地址
     * @param spuOrderId
     * @param spuId
     * @return
     */
    ApiRefundFreightAddressVO getOutRefundAddress(Long spuOrderId, Long spuId);
}
