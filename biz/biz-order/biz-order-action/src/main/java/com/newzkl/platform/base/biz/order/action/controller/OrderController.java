package com.newzkl.platform.base.biz.order.action.controller;

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
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.core.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
