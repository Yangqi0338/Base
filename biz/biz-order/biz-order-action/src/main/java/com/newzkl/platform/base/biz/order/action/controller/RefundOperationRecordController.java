package com.newzkl.platform.base.biz.order.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.order.application.service.IRefundOperationRecordService;
import com.newzkl.platform.base.biz.order.model.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.biz.order.model.vo.RefundOperationRecordVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 售后记录 协商记录
 * 处理HTTP请求，参数校验，权限控制，结果封装
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@RestController
@RequestMapping("/sale/refund/operationRecord")
public class RefundOperationRecordController {

    @Autowired
    private IRefundOperationRecordService refundOperationRecordService;

    /**
     * 按售后单ID查询操作记录列表
     * @param refundId 售后单ID
     * @return 视图对象列表
     */
    @GetMapping("listByRefundId")
    public PlatformResult<List<RefundOperationRecordVO>> listByRefundId(@RequestParam("refundId") Long refundId) {
        List<RefundOperationRecordVO> result = refundOperationRecordService.listByRefundId(refundId);
        return PlatformResult.success(result);
    }

    /**
     * 分页查询售后操作记录
     * @param req 分页查询请求对象
     * @return 分页结果（视图对象）
     */
    @PostMapping("pageQuery")
    public PlatformResult<IPage<RefundOperationRecordVO>> pageQuery(@Validated @RequestBody RefundOperationRecordPageReq req) {
        IPage<RefundOperationRecordVO> result = refundOperationRecordService.pageQuery(req);
        return PlatformResult.success(result);
    }
}