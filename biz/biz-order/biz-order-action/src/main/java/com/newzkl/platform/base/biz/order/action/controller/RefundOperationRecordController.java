package com.newzkl.platform.base.biz.order.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.action.cmd.OrderCmd;
import com.newzkl.platform.base.biz.order.application.service.RefundOperationRecordService;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundOperationRecordVO;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
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
 * 售后-协商 (操作) 记录控制器。
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.interfaces.controller.RefundOperationRecordController},
 * 7 端点路径与 HTTP 方法保持不变。旧 {@code spuOrderId} (Long) 随中台模型改为业务单号
 * {@code spuOrderNo} (String), 请求参数名沿用 {@code spuOrderId} 不破坏前端契约。
 * 分页出参由旧 {@code IPage} 壳改为 MyBatis-Plus {@code Page}。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/sale/refund/operationRecord")
@RequiredArgsConstructor
public class RefundOperationRecordController {

    private final RefundOperationRecordService refundOperationRecordService;

    /**
     * 新增售后操作记录。
     *
     * @param req 新增入参
     * @return 视图对象
     */
    @PostMapping("create")
    public ScmResult<RefundOperationRecordVO> create(@Validated @RequestBody RefundOperationRecordCreateReq req) {
        return ScmResult.success(refundOperationRecordService.create(req));
    }

    /**
     * 修改售后操作记录 (仅描述性字段)。
     *
     * @param req 修改入参
     * @return 视图对象
     */
    @PostMapping("update")
    public ScmResult<RefundOperationRecordVO> update(@Validated @RequestBody RefundOperationRecordUpdateReq req) {
        return ScmResult.success(refundOperationRecordService.update(req));
    }

    /**
     * 删除售后操作记录。
     *
     * @param idObj 主键 ID 入参
     * @return 成功结果
     */
    @PostMapping("delete")
    public ScmResult<Void> delete(@Valid @RequestBody OrderCmd.ID idObj) {
        refundOperationRecordService.delete(idObj.getId());
        return ScmResult.success();
    }

    /**
     * 按 ID 查询售后操作记录。
     *
     * @param id 主键 ID
     * @return 视图对象
     */
    @GetMapping("getById")
    public ScmResult<RefundOperationRecordVO> getById(@RequestParam("id") Long id) {
        return ScmResult.success(refundOperationRecordService.getById(id));
    }

    /**
     * 按售后单 ID 查询操作记录列表。
     *
     * @param refundId 售后单 ID
     * @return 视图对象列表
     */
    @GetMapping("listByRefundId")
    public ScmResult<List<RefundOperationRecordVO>> listByRefundId(@RequestParam("refundId") Long refundId) {
        return ScmResult.success(refundOperationRecordService.listByRefundId(refundId));
    }

    /**
     * 按 SPU 订单号查询操作记录列表。
     *
     * @param spuOrderNo SPU 订单号
     * @return 视图对象列表
     */
    @GetMapping("listBySpuOrderId")
    public ScmResult<List<RefundOperationRecordVO>> listBySpuOrderId(@RequestParam("spuOrderId") String spuOrderNo) {
        return ScmResult.success(refundOperationRecordService.listBySpuOrderNo(spuOrderNo));
    }

    /**
     * 分页查询售后操作记录。
     *
     * @param req 分页查询入参
     * @return 分页结果
     */
    @PostMapping("pageQuery")
    public ScmResult<Page<RefundOperationRecordVO>> pageQuery(
            @Validated @RequestBody RefundOperationRecordPageReq req) {
        return ScmResult.success(refundOperationRecordService.pageQuery(req));
    }
}
