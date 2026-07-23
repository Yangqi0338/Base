package com.newzkl.platform.base.biz.order.domain.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.ApplyPlatformReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundReq;
import com.newzkl.platform.base.biz.order.model.order.res.RefundAuditRes;
import com.newzkl.platform.base.biz.order.model.order.res.RefundCreateRes;
import com.newzkl.platform.base.biz.order.model.order.vo.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundFreightVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundVO;

import java.util.List;

/**
* 售后单
* @author fang
*/
public interface RefundDomain {

    /**
     * 创建
     *
     * @param refundCommand
     * @param spuOrder
     * @param skuOrders
     * @return
     */
    RefundCreateRes refundCreate(RefundReq refundCommand, SpuOrder spuOrder, List<SkuOrder> skuOrders);
    /**
     * 同意售后 api接口用
     * @param refundId
     * @param role
     * @return
     */
    RefundAuditRes agreeAudit(Long refundId, Integer role);
    /**
     * 拒绝售后 api接口用
     * @param refundId
     * @param reason
     * @return
     */
    RefundAuditRes refuseAudit(Long refundId, String reason);

    /**
     * 同意售后 给平台用
     * @param refundId
     * @param role
     * @return
     */
    RefundAuditRes agreeAuditV2(Long refundId, RoleEnum.CompanyRole role);

    /**
     * 申请平台介入
     * @param applyPlatformCommand
     */
    void applyPlatform(ApplyPlatformReq applyPlatformCommand);
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
     * 平台介入处理
     * @param refundId
     * @param execute
     */
    void platformExecute(Long refundId, Integer execute);
    /**
     * 拒绝收货
     * @param refundId
     */
    void refuseRefundFreight(Long refundId);
    /**
     * 终止售后
     * @param roleId 角色ID
     * @param accountId 用户ID
     * @param refundId 售后ID
     */
    void stopAudit(RoleEnum.CompanyRole roleId, Long accountId, Long refundId);
    /**
     * 售后已打款通知
     * @param refundId
     * @param channelId
     */
    void sellAfterRefundNotify(Long refundId, Long channelId, String outRefundId);
    /**
     * 外部供应商拒绝退货收货
     * @param refundId
     */
    void outRefuseRefundFreight(Long refundId);
    /**
     * 获取外部订单售后地址
     *
     * @param spuOrderNo
     * @param spuId
     * @return
     */
    ApiRefundFreightAddressVO getOutRefundAddress(String spuOrderNo, Long spuId);

    /**
     * 售后单-值对象
     * @param refundId
     * @return
     */
    RefundVO refundVO(Long refundId);

    /**
     * 根据spuOrderId查询售后单详情
     *
     * @param spuOrderNo@return
     */
    RefundVO refundVoBySpuOrderId(String spuOrderNo);

    /**
     * 售后单-值对象列表
     * @param refundQuery
     * @return
     */
    Page<RefundVO> refundVOList(RefundPageReq refundQuery);

    /**
     * 根据query导出excel
     */
    List<RefundExcelVO> exportRefund(RefundPageReq refundQuery);
}
