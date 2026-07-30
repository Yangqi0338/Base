package com.newzkl.platform.base.biz.order.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.OrderStateRecordService;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateRecordVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单-状态流转记录控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.interfaces.controller.OrderStateRecordController},
 * 路径与 HTTP 方法保持不变。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/sale/orderRecord")
@RequiredArgsConstructor
public class OrderStateRecordController {

    private final OrderStateRecordService orderStateRecordService;

    /**
     * 分页查询订单状态记录 (按 ID 降序)
     *
     * @param req 分页查询入参
     * @return 订单状态记录分页
     */
    @PostMapping("/page")
    public PlatformResult<Page<OrderStateRecordVO>> pageQuery(@RequestBody @Validated OrderStateRecordPageReq req) {
        return PlatformResult.success(orderStateRecordService.pageQuery(req));
    }
}
