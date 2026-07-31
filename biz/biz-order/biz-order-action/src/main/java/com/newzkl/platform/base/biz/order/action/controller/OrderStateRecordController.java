package com.newzkl.platform.base.biz.order.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.order.application.service.IOrderStateRecordService;
import com.newzkl.platform.base.biz.order.model.req.OrderStateRecordPageReq;
import com.newzkl.platform.base.biz.order.model.vo.OrderStateRecordVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单-状态记录
 * 处理HTTP请求，参数校验，权限控制，结果封装
 *
 * @author KC
 * @since 2026-07-31
 */
@RestController
@RequestMapping("/sale/orderRecord")
public class OrderStateRecordController {

    @Autowired
    private IOrderStateRecordService orderStateRecordService;

    /**
     * 按spuOrderId查询订单状态记录列表（按操作时间倒序）
     *
     * @param spuOrderId 商品订单ID（不能为空）
     * @return 订单状态记录列表
     */
    @GetMapping("listBySpuOrderId")
    public PlatformResult<List<OrderStateRecordVO>> listBySpuOrderId(@RequestParam("spuOrderId") @NotNull(message = "spuOrderId不能为空") Long spuOrderId) {
        List<OrderStateRecordVO> result = orderStateRecordService.listBySpuOrderId(spuOrderId);
        return PlatformResult.success(result);
    }

    /**
     * 分页查询订单状态记录
     *
     * @param req 分页查询请求对象
     * @return 分页结果（视图对象）
     */
    @PostMapping("pageQuery")
    public PlatformResult<IPage<OrderStateRecordVO>> pageQuery(@Validated @RequestBody OrderStateRecordPageReq req) {
        IPage<OrderStateRecordVO> result = orderStateRecordService.pageQuery(req);
        return PlatformResult.success(result);
    }
}
