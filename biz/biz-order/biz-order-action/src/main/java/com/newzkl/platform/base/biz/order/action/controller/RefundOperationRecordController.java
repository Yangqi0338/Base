package com.newzkl.platform.base.biz.order.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.action.cmd.OrderCmd;
import com.newzkl.platform.base.biz.order.application.service.RefundOperationRecordService;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundOperationRecordVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 售后-协商 (操作) 记录控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.interfaces.controller.RefundOperationRecordController},
 * 保留端点路径与 HTTP 方法不变。分页出参由旧 {@code IPage} 壳改为 MyBatis-Plus {@code Page}。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/sale/refund/operationRecord")
@RequiredArgsConstructor
public class RefundOperationRecordController {

    private final RefundOperationRecordService refundOperationRecordService;

    /**
     * 新增售后操作记录
     *
     * @param req 新增入参
     * @return 视图对象
     */
    @PostMapping("create")
    public PlatformResult<RefundOperationRecordVO> create(@Validated @RequestBody RefundOperationRecordCreateReq req) {
        return PlatformResult.success(refundOperationRecordService.create(req));
    }

    /**
     * 修改售后操作记录 (仅描述性字段)
     *
     * @param req 修改入参
     * @return 视图对象
     */
    @PostMapping("update")
    public PlatformResult<RefundOperationRecordVO> update(@Validated @RequestBody RefundOperationRecordUpdateReq req) {
        return PlatformResult.success(refundOperationRecordService.update(req));
    }

    /**
     * 删除售后操作记录
     *
     * @param idObj 主键 ID 入参
     * @return 成功结果
     */
    @PostMapping("delete")
    public PlatformResult<Void> delete(@Valid @RequestBody OrderCmd.ID idObj) {
        refundOperationRecordService.delete(idObj.getId());
        return PlatformResult.success();
    }

    /**
     * 按售后单 ID 查询操作记录列表
     *
     * @param refundId 售后单 ID
     * @return 视图对象列表
     */
    @GetMapping("listByRefundId")
    public PlatformResult<List<RefundOperationRecordVO>> listByRefundId(@RequestParam("refundId") Long refundId) {
        return PlatformResult.success(refundOperationRecordService.listByRefundId(refundId));
    }

    /**
     * 分页查询售后操作记录
     *
     * @param req 分页查询入参
     * @return 分页结果
     */
    @PostMapping("pageQuery")
    public PlatformResult<Page<RefundOperationRecordVO>> pageQuery(
            @Validated @RequestBody RefundOperationRecordPageReq req) {
        return PlatformResult.success(refundOperationRecordService.pageQuery(req));
    }
}
