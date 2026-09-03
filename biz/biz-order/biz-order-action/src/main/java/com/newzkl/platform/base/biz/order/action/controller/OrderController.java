package com.newzkl.platform.base.biz.order.action.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.action.cmd.OrderCmd;
import com.newzkl.platform.base.biz.order.application.service.CommitOrder;
import com.newzkl.platform.base.biz.order.application.service.OrderService;
import com.newzkl.platform.base.biz.order.application.service.QueryService;
import com.newzkl.platform.base.biz.order.domain.service.OrderDomain;
import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.common.core.model.req.CodeCommand;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.office.EasyExcelUtil;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.core.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;

/**
 * 交易-订单
 * @author fang
 */
@RestController
@RequestMapping("/sale/order")
@FuncPermission("订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDomain orderDomain;

    @Autowired
    private QueryService queryService;

    @Autowired
    private CommitOrder commitOrder;

    /**
     * C端下单
     * @param orderCreate
     * @return
     */
    @PostMapping("memberCreateOrder")
    @FuncPermission("C端下单")
    public PlatformResult<OrderCreateRes> memberCreateOrder(@Validated @RequestBody OrderCmd.OrderCreate orderCreate) {
        MemberOrderCreateCommand memberOrderCreateCommand = Optional.ofNullable(orderCreate.getMemberOrderCreateCommand()).orElse(new MemberOrderCreateCommand());
        memberOrderCreateCommand.setAccountId(SecurityUtils.getAccountId());
        memberOrderCreateCommand.setUserName(SecurityUtils.getUsername());
        memberOrderCreateCommand.setNickName(SecurityUtils.getNickName());
        return PlatformResult.success(commitOrder.createMemberPrePayOrder(orderCreate.getOrderCreateCommand(), memberOrderCreateCommand));
    }

    /**
     * C端消费者 查询预提交订单
     *
     * @param command 支付请求参数
     * @return 支付结果
     */
    @PostMapping("getOrderCreateRes")
    public PlatformResult<OrderCreateRes> getOrderCreateResByRedis(@RequestBody @Validated MemberOrderCreateCommand command) {
        command.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(commitOrder.getOrderCreateResByRedis(command));
    }

    /**
     * C端消费者提交订单
     *
     * @param command 支付请求参数
     * @return 支付结果
     */
    @PostMapping("commitMemberPayment")
    @FuncPermission("提交订单")
    public PlatformResult<OrderAgg> commitMemberPrePayOrder(@RequestBody @Validated CommitMemberOrderCommand command) {
        command.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(commitOrder.commitMemberPrePayOrder(command));
    }


    /**
     * C端消费者支付
     *
     * @param command 支付请求参数
     * @return 支付结果
     */
    @PostMapping("memberPayment")
    @FuncPermission("C端支付")
    public PlatformResult<PayBaseResult> consumerPayment(@RequestBody @Validated PayMemberOrderCommand command) {

        return PlatformResult.success(commitOrder.memberPayOrder(command));
    }

    /**
     * C端订单变更收货地址
     *
     * @param command
     * @return
     */
    @PostMapping("changeOrderShip")
    @FuncPermission("变更收货地址")
    public PlatformResult<Boolean> changeOrderShip(@RequestBody @Validated OrderShipCommand command) {
        command.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(commitOrder.changeOrderShip(command));
    }

    /**
     * C端取消订单
     * @param command SPU订单ID
     * @return
     */
    @PostMapping("memberCancelOrder")
    @FuncPermission("C端取消订单")
    public PlatformResult<Void> memberCancelOrder(@Validated @RequestBody MemberCancelOrderCommand command) {
        orderDomain.cancelOrder(command.getOrderNo(), command.getCancelReason());
        return PlatformResult.success();
    }
    /**
     * C端确认收货
     * @param orderNo SPU订单ID
     * @return
     */
    @PostMapping("memberConfirmOrder")
    @FuncPermission("C端确认收货")
    public PlatformResult<Void> memberConfirmOrder(@RequestParam("orderNo") String orderNo) {
        orderDomain.receiveSkuOrder(orderNo, null);
        return PlatformResult.success();
    }

    /**
     * C端再来一单
     * @param command SPU订单ID
     * @return
     */
    @PostMapping("createOrderAgain")
    @FuncPermission("再来一单")
    public PlatformResult<OrderCreateRes> createOrderAgain(@Validated @RequestBody CodeCommand command) {
        return PlatformResult.success(commitOrder.createOrderAgain(command.getCodeList()));
    }

    /**
     * 渠道商取消交易单
     * @param command 交易单ID
     * @return
     */
    @PostMapping("channelCancelOrder")
    @FuncPermission("渠道商取消交易单")
    public PlatformResult<Void> channelCancelOrder(@Validated @RequestBody CodeCommand command) {
        orderDomain.channelCancelOrder(command.getCode(),"门店主动取消订单" );
        return PlatformResult.success();
    }

    /**
     * 交易单分页
     * @param orderQuery 交易单查询条件
     * @return 交易单聚合分页
     */
    @PostMapping("orderPage")
    public PlatformResult<Page<OrderAggVO>> orderPage(@RequestBody OrderQuery orderQuery) {
        boolean b = appendOrderQuery(orderQuery);
        if (!b) {
            return PlatformResult.success(new Page<>());
        }
        Page<OrderAggVO> listOrder = queryService.orderAggVOList(orderQuery);
        return PlatformResult.success(listOrder);
    }

    /**
     * 交易单详情
     * @param orderNo 交易单号
     * @return 交易单聚合
     */
    @GetMapping("orderVO")
    public PlatformResult<OrderAggVO> orderVO(@RequestParam("orderNo") String orderNo) {
        OrderAggVO orderAggVO = queryService.orderAggVO(orderNo);
        return PlatformResult.success(orderAggVO);
    }

    /**
     * 交易单余额支付
     *
     * @param command
     * @return
     */
    @PostMapping("orderBalancePay")
    @FuncPermission("交易单余额支付")
    public PlatformResult<Void> orderBalancePay(@RequestBody CodeCommand command) {
        if (AccountEnum.Identity.CHANNEL == SecurityUtils.getIdentity()) {
            orderService.orderBalancePay(command.getCodeList().toArray(new String[0]));
        }
        return PlatformResult.success();
    }
    /**
     * 交易单直接支付(渠道商对 C 端待付款订单直接确认支付, 跳过采购金扣减)
     */
    @PostMapping("orderDirectPay")
    @FuncPermission("交易单直接支付")
    public PlatformResult<Void> orderDirectPay(@RequestBody CodeCommand command) {
        if (AccountEnum.Identity.CHANNEL == SecurityUtils.getIdentity()) {
            orderService.orderDirectPay(command.getCode());
        }
        return PlatformResult.success();
    }
    /**
     * 交易单导出
     */
    @PostMapping("exportOrder")
    public void exportOrder(HttpServletResponse response, @RequestBody OrderQuery orderQuery) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("订单", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), OrderExcelVO.class).sheet("模板").doWrite(data(orderQuery));
    }

    /**
     * 交易单明细导出
     */
    @PostMapping("exportOrderItem")
    public void exportOrderItem(@RequestBody OrderQuery orderQuery) throws IOException {
        appendOrderQuery(orderQuery);
        Set<Integer> yellowRowsSet = new HashSet<>();
        RowBackGroundWriteHandler handler = new RowBackGroundWriteHandler(yellowRowsSet, IndexedColors.YELLOW.index);
        List<OrderItemExcelVO> data = orderDomain.queryOrderItemExcelVO(orderQuery);
        for (int i = 0; i < data.size(); i++) {
            OrderItemExcelVO orderItemExcelVO = data.get(i);
            if(orderItemExcelVO.getRefundingCount() != null && orderItemExcelVO.getRefundingCount() > 0){
                yellowRowsSet.add(i + 1);
            }
        }
        EasyExcelUtil.export(data, handler, "订单明细");
    }

    private boolean appendOrderQuery(OrderQuery orderQuery) {
        Long accountId = SecurityUtils.getAccountId();
        AccountEnum.Identity identity = SecurityUtils.getIdentity();
        if (AccountEnum.Identity.CHANNEL == identity) {
            orderQuery.setChannelId(accountId);
            if(OrderEnum.OrderType.MEMBER == orderQuery.getOrderType() && orderQuery.getStoreId() == null){
                orderQuery.setStoreId(accountId);
            }
        } else if (AccountEnum.Identity.PLATFORM == identity) {
            orderQuery.setSpuChannelType(null);
        } else if (AccountEnum.Identity.SUPPLIER == identity) {
            orderQuery.setSupplierId(accountId);
            orderQuery.setOrderStateList(Arrays.asList(
                    OrderEnum.State.WAIT_DELIVERY,
                    OrderEnum.State.WAIT_RECEIVE,
                    OrderEnum.State.DOWN_RECEIVE,
                    OrderEnum.State.SUCCESS,
                    OrderEnum.State.CLOSE
            ));
        } else if (AccountEnum.Identity.MEMBER == identity) {
            orderQuery.setMemberId(accountId);

        }

        return true;
    }

    private List<OrderExcelVO> data(OrderQuery orderQuery) {
        appendOrderQuery(orderQuery);
        Page<OrderAggVO> listOrder = queryService.orderAggVOList(orderQuery);
        List<OrderAggVO> list = listOrder.getRecords();
        List<OrderExcelVO> excelDate = new ArrayList<>();
        for (OrderAggVO orderAggVO : list) {
            OrderExcelVO orderExcelVO = new OrderExcelVO();
            orderExcelVO.setId(orderAggVO.getOrderVO().getId().toString());
            orderExcelVO.setOutOrderNo(orderAggVO.getOrderVO().getOutOrderNo());
            BigDecimal supplierAmount = orderAggVO.getOrderVO().getSupplierAmount().getAmount().setScale(2, RoundingMode.DOWN);
            orderExcelVO.setSupplierAmount(supplierAmount.toPlainString());
            BigDecimal freightAmount = orderAggVO.getOrderVO().getFreightAmount().getAmount().setScale(2, RoundingMode.DOWN);
            orderExcelVO.setFreightAmount(freightAmount.toPlainString());
            Integer skuCount = 0;
            if(orderAggVO.getSkuOrderList() != null){
                for (SkuOrderVO skuOrderVO : orderAggVO.getSkuOrderList()) {
                    skuCount = skuCount + skuOrderVO.getCount();
                }
            }
            orderExcelVO.setCount(String.valueOf(skuCount));
            orderExcelVO.setOrderState(orderAggVO.getOrderVO().getOrderState().getValue());
            ShipVO shipVO = JSON.parseObject(orderAggVO.getOrderVO().getShipVO(), ShipVO.class);
            orderExcelVO.setShipName(shipVO.getShipName());
            orderExcelVO.setShipPhone(shipVO.getShipPhone());
            String shipAddress = shipVO.getShipAddress() == null ? "":shipVO.getShipAddress();
            orderExcelVO.setShipArea(shipVO.getShipArea() + "," + shipAddress);
            orderExcelVO.setSpuName(orderAggVO.getOrderVO().getSpuName());
            orderExcelVO.setRemark(orderAggVO.getOrderVO().getRemark());
            excelDate.add(orderExcelVO);
        }
        return excelDate;
    }

    /**
     * 统计渠道商所有订单状态数量
     * @return 订单状态统计列表
     */
    @GetMapping("/countOrderState")
    public PlatformResult<List<OrderStateCountVO>> countOrderState() {
        // 获取当前渠道商ID
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setChannelId(SecurityUtils.getAccountId());
        List<OrderStateCountVO> stateCountList = orderService.countOrderState(orderQuery);
        return PlatformResult.success(stateCountList);
    }

    /**
     * 统计消费者所有订单状态数量
     * @return 订单状态统计列表
     */
    @GetMapping("/countState")
    public PlatformResult<List<OrderStateCountVO>> countState() {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setMemberId(SecurityUtils.getAccountId());
        List<OrderStateCountVO> stateCountList = orderService.countOrderState(orderQuery);
        return PlatformResult.success(stateCountList);
    }
}
