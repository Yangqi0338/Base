package com.newzkl.platform.base.biz.order.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkl.scm.finance.model.pay.res.TradeBaseRes;
import com.newzkl.platform.base.biz.order.model.order.req.*;
import com.newzkl.platform.base.biz.order.model.order.res.CreateOrderRes;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateCountVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderVO;

import java.util.List;

/**
 * 订单服务
 * @author sijiwang
 */
public interface IOrderService {

    /**
     * 创建预订单
     *
     * @param req 创建订单请求参数
     * @return 创建订单结果
     */
    CreateOrderRes createOrderPre(CreateOrderReq req);

    /**
     * 提交预订单
     *
     * @param req 提交订单请求参数
     * @return 提交订单结果
     */
    CreateOrderRes commitOrderPre(CommitOrderPreReq req);

    /**
     * 支付订单
     *
     * @param req 支付订单请求参数
     * @return 支付订单结果
     */
    TradeBaseRes payOrder(PayOrderReq req);

    /**
     * 再来一单
     * @param spuOrderNo
     * @return
     */
    CreateOrderRes createOrderAgain(String spuOrderNo);

    /**
     * 查询预订单
     *
     * @param req 查询订单请求参数
     * @return 查询订单结果
     */
    CreateOrderRes selectOrderPre(CommitOrderPreReq req);

    /**
     * 查询SPU订单列表
     *
     * @param req 查询SPU订单列表请求参数
     * @return 查询SPU订单列表结果
     */
    Page<SpuOrderVO> spuPage(SpuOrderPageReq req);

    /**
     * 获取SPU订单详情
     *
     * @param spuOrderNo SPU订单号
     * @return SPU订单详情
     */
    SpuOrderVO getSpuOrderDetail(String spuOrderNo);

    /**
     * 统计指定渠道的订单状态数量
     * @param channelId 渠道ID
     * @return 订单状态统计列表
     */
    List<OrderStateCountVO> countOrderStateByChannel(Long channelId);

    /**
     * 统计指定账户的订单状态数量
     * @param accountId 账户ID
     * @return 订单状态统计列表
     */
    List<OrderStateCountVO> countOrderStateByAccount(Long accountId);

    /**
     * 订单余额支付
     *
     * @param orderNos 订单ID
     */
    void orderBalancePay(String... orderNos);

    /**
     * 运营商交易单余额支付
     *
     * @param orderNo 订单ID
     */
    void operatorOrderBalancePay(String orderNo);

    /**
     * 订单直接支付
     *
     * @param orderNo 订单ID
     */
    void orderDirectPay(String orderNo);

    /**
     * 修改订单收货地址
     * @param req 地址修改请求
     * @return 是否修改成功
     */
    Boolean changeOrderShip(OrderAddressUpdateReq req);

    /**
     * 取消订单
     * @param req 取消订单请求
     * @return 是否取消成功
     */
    Boolean cancelOrder(CancelOrderReq req);

    /**
     * 确认收货
     * @param req 确认收货请求
     * @return 是否确认成功
     */
    Boolean confirmOrder(ConfirmOrderReq req);

    /**
     * 完成订单
     * @param req 完成订单请求
     * @return 是否完成成功
     */
    Boolean completeOrder(ConfirmOrderReq req);
}
