package com.newzkl.platform.base.biz.order.application.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.biz.order.model.order.req.ApplyPlatformReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundReq;
import com.newzkl.platform.base.biz.order.model.order.vo.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundFreightVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundVO;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 售后应用服务层接口
 * @date 2023/12/8 11:16
 */
public interface RefundService {
    /**
     * 售后单创建（API接口）
     * @param refundCommand
     * @return
     */
    Long refundCreateApi(RefundReq refundCommand);

    /**
     * 售后单创建
     * @param refundCommand
     * @return
     */
    Long refundCreate(RefundReq refundCommand);

    /**
     * 供应商审核
     * @param refundId
     * @param execute
     * @param isAudit
     */
    void supplierAudit(Long refundId, Integer execute,boolean isAudit);

    /**
     * 渠道审核
     * @param refundId
     * @param spuOrderNo
     * @param execute
     * @param reason
     * @param isAudit
     */
    void channelAudit(Long refundId,String spuOrderNo, Integer execute, String reason,boolean isAudit);

    /**
     * 供应商确认退货收货
     * @param refundId
     */
    void supplierConfirmRefundFreight(Long refundId);

    /**
     * 商户确认退货收货
     * @param id
     */
    void merchantConfirmRefundFreight(Long id);

    /**
     * 售后单-值对象
     * @param refundId
     * @return
     */
    RefundVO refundVO(Long refundId);

    /**
     * 根据spuOrderId查询售后单详情
     *
     * @param spuOrderNo
     * @return
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

    // ==================== 以下是补齐的全部缺失方法 ====================

    /**
     * 申请平台介入
     * @param applyPlatformCommand
     */
    void applyPlatform(ApplyPlatformReq applyPlatformCommand);

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
     * 获取外部订单售后地址
     * @param spuOrderNo
     * @param spuId
     * @return
     */
    ApiRefundFreightAddressVO getOutRefundAddress(String spuOrderNo, Long spuId);

    /**
     * 外部供应商拒绝退货收货
     * @param refundId
     */
    void outRefuseRefundFreight(Long refundId);
}