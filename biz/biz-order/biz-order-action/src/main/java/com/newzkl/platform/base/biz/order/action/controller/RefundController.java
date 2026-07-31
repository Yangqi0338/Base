package com.newzkl.platform.base.biz.order.action.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.action.cmd.RefundCmd;
import com.newzkl.platform.base.biz.order.application.service.IRefundService;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IRefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.IRefundDomain;
import com.newzkl.platform.base.biz.order.model.req.RefundCommand;
import com.newzkl.platform.base.biz.order.model.req.RefundQuery;
import com.newzkl.platform.base.biz.order.model.vo.RefundFreightVO;
import com.newzkl.platform.base.biz.order.model.vo.RefundVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
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
public class RefundController {

    @Autowired
    private IRefundService refundService;
    @Autowired
    private IRefundDomain refundDomain;
    @Autowired
    private IRefundRepository refundRepository;

    /**
     * C端创建售后
     * @param refundCommand
     * @return
     */
    @PostMapping("refundCreate")
    public PlatformResult<Long> refundCreate(@Validated @RequestBody RefundCommand refundCommand) {
        refundCommand.setRole(RoleEnum.CompanyRole.MEMBER);
        Long refundId = refundService.refundCreate(refundCommand);
        return PlatformResult.success(refundId);
    }
    /**
     * C端取消售后
     * @param idObj 售后单ID
     * @return
     */
    @PostMapping("memberCancelRefund")
    public PlatformResult<Void> memberCancelRefund(@Validated @RequestBody IdListCommand idObj) {
        refundDomain.stopAudit(RoleEnum.CompanyRole.MEMBER, SecurityUtils.getAccountId(), idObj.getId());
        return PlatformResult.success();
    }
    /**
     * 渠道商审核售后单
     * @param audit
     * @return
     */
    @PostMapping("channelAudit")
    public PlatformResult<Void> channelAudit(@Validated @RequestBody RefundCmd.Audit audit) {
        refundService.channelAudit(audit.getRefundId(),audit.getSpuOrderId() , audit.getExecute(), audit.getReason(), false);
        return PlatformResult.success();
    }
    /**
     * 供应商审核售后单
     * @param audit
     * @return
     */
    @PostMapping("supplierAudit")
    public PlatformResult<Void> supplierAudit(@Validated @RequestBody RefundCmd.Audit audit) {
        refundService.supplierAudit(audit.getRefundId(), audit.getExecute(), false);
        return PlatformResult.success();
    }
    /**
     * 提交退货物流
     * @param refundFreightVO
     * @return
     */
    @PostMapping("submitRefundFreight")
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
    public PlatformResult<Void> supplierConfirmRefundFreight(@Validated @RequestBody IdListCommand idObj) {
        refundService.supplierConfirmRefundFreight(idObj.getId());
        return PlatformResult.success();
    }
    /**
     * 供应商拒绝收货
     * @param idObj
     * @return
     */
    @PostMapping("supplierRefuseRefundFreight")
    public PlatformResult<Void> supplierRefuseRefundFreight(@Validated @RequestBody IdListCommand idObj) {
        refundDomain.refuseRefundFreight(idObj.getId());
        return PlatformResult.success();
    }

    /**
     * 售后单详情
     * @param refundId
     * @return
     */
    @GetMapping("refund")
    public PlatformResult<RefundVO> refund(@RequestParam("id") Long refundId) {
        RefundVO refund = refundRepository.refundVO(refundId);
        return PlatformResult.success(refund);
    }

    /**
     * 根据BySpuOrderId查售后单详情
     * @param spuOrderId
     * @return
     */
    @GetMapping("refundBySpuOrderId")
    public PlatformResult<RefundVO> refundVoBySpuOrderId(@RequestParam("spuOrderId") Long spuOrderId) {
        RefundVO refund = refundRepository.refundVoBySpuOrderId(spuOrderId);
        return PlatformResult.success(refund);
    }

    private static void appendRefundQuery(RefundQuery refundQuery) {
        if(RoleEnum.CompanyRole.CHANNEL == SecurityUtils.getRole()){
            refundQuery.setChannelId(SecurityUtils.getAccountId());
        }else if(RoleEnum.CompanyRole.PLATFORM == SecurityUtils.getRole()){

        }else if(RoleEnum.CompanyRole.SUPPLIER == SecurityUtils.getRole()){
            refundQuery.setSupplierId(SecurityUtils.getAccountId());
            refundQuery.setFromOrderStateNot(Collections.singletonList(OrderEnum.State.SENDING));
        }else if(RoleEnum.CompanyRole.MEMBER == SecurityUtils.getRole()){
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
        Page<RefundVO> listRefund = refundRepository.refundVOList(refundQuery);
        return PlatformResult.success(listRefund);
    }
}
