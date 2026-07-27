package com.newzkl.platform.base.biz.order.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.OrderStateRecordService;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateRecordVO;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import jakarta.validation.constraints.NotNull;
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
 * 订单-状态流转记录控制器。
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.interfaces.controller.OrderStateRecordController},
 * 路径与 HTTP 方法保持不变。旧入参 {@code spuOrderId} 为 {@code Long}, 中台模型已统一改用
 * 业务单号 {@code spuOrderNo} ({@code String}), 故参数名保留、类型放宽为 {@code String}。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/sale/orderRecord")
@RequiredArgsConstructor
public class OrderStateRecordController {

    private final OrderStateRecordService orderStateRecordService;

    /**
     * 按 SPU 订单号查询状态记录列表 (按操作时间倒序)。
     *
     * @param spuOrderNo SPU 订单号 (不能为空)
     * @return 订单状态记录列表
     */
    @GetMapping("/listBySpuOrderId")
    public ScmResult<List<OrderStateRecordVO>> listBySpuOrderId(
            @RequestParam("spuOrderId") @NotNull(message = "spuOrderId不能为空") String spuOrderNo) {
        return ScmResult.success(orderStateRecordService.listBySpuOrderNo(spuOrderNo));
    }

    /**
     * 分页查询订单状态记录 (按 ID 降序)。
     *
     * @param req 分页查询入参
     * @return 订单状态记录分页
     */
    @PostMapping("/page")
    public ScmResult<Page<OrderStateRecordVO>> pageQuery(@RequestBody @Validated OrderStateRecordPageReq req) {
        return ScmResult.success(orderStateRecordService.pageQuery(req));
    }
}
