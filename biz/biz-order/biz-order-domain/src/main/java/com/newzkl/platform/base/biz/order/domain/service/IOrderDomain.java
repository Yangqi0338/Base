package com.newzkl.platform.base.biz.order.domain.service;



import com.newzkl.platform.base.biz.order.model.dto.IndexCountRes;
import com.newzkl.platform.base.biz.order.model.dto.Order;
import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.res.*;
import com.newzkl.platform.base.biz.order.model.support.api.SkuSaleInfo;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckRes;
import com.newzkl.platform.base.biz.order.model.vo.DeliverVO;
import com.newzkl.platform.base.biz.order.model.vo.OrderStateCountVO;
import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderItemExcelVO;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2024/3/129:31
 */
public interface IOrderDomain {
    /**
     * 创建订单
     * @param data
     * @param orderCreateCommand
     * @param memberOrderCreateCommand
     * @return
     */
    OrderCreateRes createOrder(OrderGoodsCheckRes data, OrderCreateCommand orderCreateCommand, MemberOrderCreateCommand memberOrderCreateCommand);


    /**
     * 订单聚合保存
     * @param orderAgg
     */
    void orderAggSave(OrderAgg orderAgg);
    /**
     * 派发订单
     * @param orderIdList
     */
    void sendOrder(List<Long> orderIdList);

    /**
     * 发货SKU
     * @param deliverCommand
     * @return
     */
    DeliverRes deliverCreate(DeliverCommand deliverCommand);
    /**
     * 收货SKU订单
     * @param spuOrderId
     * @param skuOrderIdList
     * @return
     */
    ReceiveSkuOrderRes receiveSkuOrder(Long spuOrderId, List<Long> skuOrderIdList);
    /**
     * 完成SKU订单
     * @param spuOrderId
     * @param skuOrderIdList
     * @return
     */
    CompleteSkuOrderRes completeSkuOrder(Long spuOrderId, List<Long> skuOrderIdList);
    /**
     * 消费者取消交易单
     *
     * @param spuOrderId
     * @param cancelReason
     */
    void memberCancelOrder(Long spuOrderId, String cancelReason);

    /**
     * 渠道取消交易单
     *
     * @param spuOrderId
     * @param cancelReason
     */
    void channelCancelOrder(Long spuOrderId, String cancelReason);

    TripSpuOrderChangeRes tripSpuOrderChange(List<Long> orderId, List<Long> spuOrderId, List<Long> skuOrderId);

    void freightSettleSuccessNotify(List<Long> spuIdList);

    /**
     * 保存预支付单
     * @param order
     * @param memberOrderCreateCommand
     */
    void savePrePayOrder(OrderCreateRes order,MemberOrderCreateCommand memberOrderCreateCommand);

    /**
     * 获取C端用户预支付单
     * @param memberOrderCreateCommand
     * @return
     */
    OrderCreateRes getPrePayOrder(MemberOrderCreateCommand memberOrderCreateCommand);

    /**
     * 删除预支付单
     * @param memberOrderCreateCommand
     */
    void delPrePayOrder(MemberOrderCreateCommand memberOrderCreateCommand);

    void deliverEdit(DeliverCodeCommand deliverCommand);

    /**
     * 订单收货地址修改 - 核心校验（整合所有前置校验逻辑，返回运费校验结果）
     *
     * @param orderAgg 订单聚合对象
     * @param shipVO
     * @return 各SPU对应的最新运费（用于对比是否变动）
     */
    Map<Long, Integer> validateOrderShipChange(OrderAgg orderAgg, ShipVO shipVO);

    /**
     * 校验运费是否变动（独立封装，便于两处调用）
     * @param spuOrderList SPU订单列表
     * @param goodsFreight 最新运费信息
     */
    void validateFreightUnchanged(List<SpuOrder> spuOrderList, Map<Long, Integer> goodsFreight);

    /**
     * 数据库层修改订单收货地址（最终执行更新）
     * @param orderId 订单ID
     * @param shipVOJson 收货地址JSON串
     */
    void updateOrderShipDb(Long orderId, String shipVOJson);


    /**
     * 修改订单收货信息
     * @param command
     * @return
     */
    Boolean changeOrderShip(OrderShipCommand command);

    /**
     * 订单-实体
     * @param orderId
     * @return
     */
    Order order(Long orderId);

    /**
     * 订单聚合
     * @param orderId
     * @return
     */
    OrderAgg orderAgg(Long orderId);

    /**
     * 批量修改订单状态
     *
     * @param orderIdList
     * @param sourceState
     * @param toState
     * @param spuOrderExt
     */
    void batchUpdateOrderState(List<Long> orderIdList, OrderEnum.State sourceState, OrderEnum.State toState,String spuOrderExt);

    List<SpuOrder> listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State orderState, LocalDateTime updateTime);

    void orderEdit(Order orderEdit);

    List<OrderStateCountVO> countOrderStateByChannel(Long channelId);

    List<OrderStateCountVO> countOrderStateByAccount(Long accountId);
    /**
     * 订单统计数据
     * @param timeQuery
     * @return
     */
    IndexCountRes indexCount(TimeQuery timeQuery);
    /**
     * SPU订单发货信息
     * @param spuOrderId
     * @return
     */
    Map<Long, List<DeliverVO>> orderDeliverInfo(Long spuOrderId);

    Long spuOrderId(Long orderId, Long skuId);

    List<SpuOrderItemExcelVO> querySpuOrderItemExcelVO(SpuOrderQuery spuOrderQuery);
}
