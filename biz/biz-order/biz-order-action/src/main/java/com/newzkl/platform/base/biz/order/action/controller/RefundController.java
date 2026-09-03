package com.newzkl.platform.base.biz.order.action.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.action.cmd.RefundCmd;
import com.newzkl.platform.base.biz.order.application.service.RefundService;
import com.newzkl.platform.base.biz.order.domain.service.RefundDomain;
import com.newzkl.platform.base.biz.order.model.req.ApplyPlatformCommand;
import com.newzkl.platform.base.biz.order.model.req.RefundCommand;
import com.newzkl.platform.base.biz.order.model.req.query.RefundOperationRecordQuery;
import com.newzkl.platform.base.biz.order.model.req.query.RefundQuery;
import com.newzkl.platform.base.biz.order.model.vo.RefundFreightVO;
import com.newzkl.platform.base.biz.order.model.vo.RefundOperationRecordVO;
import com.newzkl.platform.base.biz.order.model.vo.RefundVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.order.ExpressEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.core.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 交易-售后
 * @author fang
 */
@RestController
@RequestMapping("/sale/refund")
@FuncPermission("售后管理")
public class RefundController {

    @Autowired
    private RefundService refundService;
    @Autowired
    private RefundDomain refundDomain;

    /**
     * C端创建售后
     * @param refundCommand
     * @return
     */
    @PostMapping("refundCreate")
    @FuncPermission("C端创建售后")
    public PlatformResult<Long> refundCreate(@Validated @RequestBody RefundCommand refundCommand) {
        refundCommand.setIdentity(AccountEnum.Identity.MEMBER);
        Long refundId = refundService.refundCreate(refundCommand);
        return PlatformResult.success(refundId);
    }
    /**
     * 取消售后(会员/渠道商统一入口, 按 token 身份分流)
     * @param idObj 售后单ID
     * @return
     */
    @PostMapping("cancelRefund")
    @FuncPermission("取消售后")
    public PlatformResult<Void> cancelRefund(@Validated @RequestBody IdCommand idObj) {
        refundDomain.stopAudit(SecurityUtils.getIdentity(), SecurityUtils.getAccountId(), idObj.getId());
        return PlatformResult.success();
    }
    /**
     * 售后审核(渠道商/供应商统一入口, 按 token 身份分流)
     * @param audit
     * @return
     */
    @PostMapping("audit")
    @FuncPermission("售后审核")
    public PlatformResult<Void> audit(@Validated @RequestBody RefundCmd.Audit audit) {
        refundService.audit(SecurityUtils.getIdentity(), audit.getRefundId(), audit.getOrderNo(), audit.getExecute(), audit.getReason(), false);
        return PlatformResult.success();
    }
    /**
     * 提交退货物流
     * @param refundFreightVO
     * @return
     */
    @PostMapping("submitRefundFreight")
    @FuncPermission("提交退货物流")
    public PlatformResult<Void> submitRefundFreight(@Validated @RequestBody RefundFreightVO refundFreightVO) {
        refundDomain.submitRefundFreight(refundFreightVO);
        return PlatformResult.success();
    }
    /**
     * 供应商确认收货
     * @param idObj
     * @return
     */
    @PostMapping("supplierConfirmRefundFreight")
    @FuncPermission("供应商确认收货")
    public PlatformResult<Void> supplierConfirmRefundFreight(@Validated @RequestBody IdCommand idObj) {
        refundService.supplierConfirmRefundFreight(idObj.getId());
        return PlatformResult.success();
    }
    /**
     * 供应商拒绝收货
     * @param idObj
     * @return
     */
    @PostMapping("supplierRefuseRefundFreight")
    @FuncPermission("供应商拒绝收货")
    public PlatformResult<Void> supplierRefuseRefundFreight(@Validated @RequestBody IdCommand idObj) {
        refundDomain.refuseRefundFreight(idObj.getId());
        return PlatformResult.success();
    }
    /**
     * 申请平台介入
     * @param applyPlatformCommand
     * @return
     */
    @PostMapping("applyPlatform")
    @FuncPermission("申请平台介入")
    public PlatformResult<Void> applyPlatform(@Validated @RequestBody ApplyPlatformCommand applyPlatformCommand) {
        refundDomain.applyPlatform(applyPlatformCommand);
        return PlatformResult.success();
    }
    /**
     * 平台介入处理
     * @param platformExecute
     * @return
     */
    @PostMapping("platformExecute")
    @FuncPermission("平台介入处理")
    public PlatformResult<Void> platformExecute(@Validated @RequestBody RefundCmd.PlatformExecute platformExecute) {
        refundDomain.platformExecute(platformExecute.getRefundId(), platformExecute.getExecute());
        return PlatformResult.success();
    }
    /**
     * 所有物流公司
     * @return
     */
    @GetMapping("allExpress")
    public PlatformResult<List<Map<String, String>>> allExpress() {
        return PlatformResult.success(ExpressEnum.ExpressType.listAllWithMap());
    }

    /**
     * 售后单详情
     * @param refundId
     * @return
     */
    @GetMapping("refund")
    public PlatformResult<RefundVO> refund(@RequestParam("id") Long refundId) {
        RefundVO refund = refundDomain.refundVO(refundId);
        return PlatformResult.success(refund);
    }

    /**
     * 根据交易单号查售后单详情
     * @param orderNo 交易单号
     * @return
     */
    @GetMapping("refundByOrderNo")
    public PlatformResult<RefundVO> refundVoByOrderNo(@RequestParam("orderNo") String orderNo) {
        RefundVO refund = refundDomain.refundVoByOrderNo(orderNo);
        return PlatformResult.success(refund);
    }

    private static void appendRefundQuery(RefundQuery refundQuery) {
        if(AccountEnum.Identity.CHANNEL == SecurityUtils.getIdentity()){
            refundQuery.setChannelId(SecurityUtils.getAccountId());
        }else if(AccountEnum.Identity.PLATFORM == SecurityUtils.getIdentity()){

        }else if(AccountEnum.Identity.SUPPLIER == SecurityUtils.getIdentity()){
            refundQuery.setSupplierId(SecurityUtils.getAccountId());
            refundQuery.setFromOrderStateNot(Collections.singletonList(OrderEnum.State.SENDING));
        }else if(AccountEnum.Identity.MEMBER == SecurityUtils.getIdentity()){
            refundQuery.setMemberId(SecurityUtils.getAccountId());
        }else {
            ThrowsException.exception(BaseErrorCode.NOT_SERVICE);
        }
    }

    /**
     * 售后单分页
     * @param refundQuery
     * @return
     */
    @PostMapping("refundPage")
    public PlatformResult<Page<RefundVO>> refundPage(@RequestBody RefundQuery refundQuery) {
        appendRefundQuery(refundQuery);
        Page<RefundVO> listRefund = refundDomain.refundPage(refundQuery);
        return PlatformResult.success(listRefund);
    }

    /**
     * 按售后单ID查询操作记录列表
     * @param refundId 售后单ID
     * @return 视图对象列表
     */
    @GetMapping("operationRecord/listByRefundId")
    public PlatformResult<List<RefundOperationRecordVO>> listRecordByRefundId(@RequestParam("refundId") Long refundId) {
        return PlatformResult.success(refundDomain.listRecordByRefundId(refundId));
    }

    /**
     * 分页查询售后操作记录
     * @param query 分页查询请求对象
     * @return 分页结果（视图对象）
     */
    @PostMapping("operationRecord/pageQuery")
    public PlatformResult<Page<RefundOperationRecordVO>> recordPage(@Validated @RequestBody RefundOperationRecordQuery query) {
        return PlatformResult.success(refundDomain.recordPage(query));
    }
}
