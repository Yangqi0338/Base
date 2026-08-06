package com.newzkl.platform.base.biz.order.domain.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordDTO;
import com.newzkl.platform.base.biz.order.model.req.RefundCommand;
import com.newzkl.platform.base.biz.order.model.req.query.RefundOperationRecordQuery;
import com.newzkl.platform.base.biz.order.model.req.query.RefundQuery;
import com.newzkl.platform.base.biz.order.model.res.RefundAuditRes;
import com.newzkl.platform.base.biz.order.model.res.RefundCreateRes;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;

import java.util.List;

/**
* 售后单
* @author fang
*/
public interface RefundDomain {

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
     * 售后关闭修改订单
     * @param spuOrderId spu订单id
     * @param item 售后数据
     */
    void skuOrderEditForRefundClose(Long spuOrderId, List<RefundItemVO> item);

    /**
     * 售后完成修改订单
     * @param spuOrderId spu订单id
     * @param item 售后数据
     */
    void skuOrderEditForRefundPass(Long spuOrderId, List<RefundItemVO> item);

    /**
     * 售后已打款通知
     * @param refundId
     * @param channelId
     */
    void sellAfterRefundNotify(Long refundId, Long channelId, String outRefundId);

	RefundVO refundVO(Long refundId);

    RefundVO refundVoBySpuOrderId(Long spuOrderId);

    Page<RefundVO> refundPage(RefundQuery refundQuery);

    /**
     * 新增售后操作记录（含领域规则校验）
     * @param entity 领域模型
     * @return 新增后的模型
     */
    Long createRecord(RefundOperationRecordDTO entity);

    /**
     * 按售后单ID查询操作记录列表（按操作时间倒序）
     * @param refundId 售后单ID
     * @return 操作记录列表
     */
    List<RefundOperationRecordVO> listRecordByRefundId(Long refundId);

    /**
     * 分页查询售后操作记录
     * @param query 分页参数
     * @return 分页结果
     */
    Page<RefundOperationRecordVO> recordPage(RefundOperationRecordQuery query);
}
