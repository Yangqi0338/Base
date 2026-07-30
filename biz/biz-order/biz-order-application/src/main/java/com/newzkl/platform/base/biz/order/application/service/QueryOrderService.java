package com.newzkl.platform.base.biz.order.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.CommitOrderPreReq;
import com.newzkl.platform.base.biz.order.model.order.req.SpuOrderPageReq;
import com.newzkl.platform.base.biz.order.model.order.res.CreateOrderRes;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateCountVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderItemExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderVO;

import java.util.List;
import java.util.Map;

/**
 * @author sijiwang
 */
public interface QueryOrderService {

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
     * 获取SPU订单
     *
     * @param spuOrderNo SPU订单号
     * @return SPU订单
     */
    SpuOrder selectSpuOrder(String spuOrderNo);

    /**
     * 根据交易单号查询SKU订单列表
     *
     * @param spuOrderNo 交易单号
     * @return 领域模型列表
     */
    List<SkuOrder> getBySpuOrderNo(String spuOrderNo);

    /**
     * 运营商视角统计各订单状态的 SPU 订单数
     *
     * <p>迁移自旧 {@code IOrderService#spuOrderStateCountMap}。</p>
     *
     * @param req                查询条件, 调用方已按业务预置可见状态集合
     * @param operatorAccountId 当前登录运营商账号 ID
     * @return 订单状态 → 订单数, 无数据的状态补 0
     */
    Map<Integer, Integer> spuOrderStateCountMap(SpuOrderPageReq req, Long operatorAccountId);

    /**
     * 装配 SPU 订单导出行
     *
     * <p>迁移自旧 {@code OrderController#data}: 取当页 SPU 订单, 逐条换算金额、状态文案、
     * 收货信息与 SKU 数量合计。</p>
     *
     * @param req 查询条件, 调用方已按登录角色收敛可见范围
     * @return 导出行列表, 恒非 null
     */
    List<SpuOrderExcelVO> exportSpuOrder(SpuOrderPageReq req);

    /**
     * 装配 SPU 订单明细 (SKU 粒度) 导出行
     *
     * <p>迁移自旧 {@code IQueryService#querySpuOrderItemExcelVO}: 先按条件取全部命中 SPU 订单
     * (不分页), 再取其下 SKU 子单逐条装配。</p>
     *
     * @param req 查询条件, 调用方已按登录角色收敛可见范围
     * @return 导出行列表, 恒非 null
     */
    List<SpuOrderItemExcelVO> exportSpuOrderItem(SpuOrderPageReq req);
}
