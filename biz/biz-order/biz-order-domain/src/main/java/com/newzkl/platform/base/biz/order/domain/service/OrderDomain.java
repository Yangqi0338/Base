package com.newzkl.platform.base.biz.order.domain.service;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.OrderStateRecordQuery;
import com.newzkl.platform.base.biz.order.model.res.*;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckRes;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckV2Res;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2024/3/129:31
 */
public interface OrderDomain {
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
     * @param orderNoList
     */
    void sendOrder(List<String> orderNoList);

    /**
     * 发货SKU
     * @param deliverCommand
     * @return
     */
    DeliverRes deliverCreate(DeliverCommand deliverCommand);
    /**
     * 收货SKU订单
     * @param orderNo 交易单ID
     * @param skuOrderNoList
     * @return
     */
    ReceiveSkuOrderRes receiveSkuOrder(String orderNo, List<String> skuOrderNoList);
    /**
     * 完成SKU订单
     * @param orderNo 交易单ID
     * @param skuOrderNoList
     * @return
     */
    CompleteSkuOrderRes completeSkuOrder(String orderNo, List<String> skuOrderNoList);
    /**
     * 消费者取消交易单
     *
     * @param orderNo 交易单ID
     * @param cancelReason
     */
    void cancelOrder(String orderNo, String cancelReason);

    /**
     * 渠道取消交易单
     *
     * @param orderNo 交易单ID
     * @param cancelReason
     */
    void channelCancelOrder(String orderNo, String cancelReason);

    /**
     * 订单状态巡检变更
     *
     * <p>SpuOrder 层折叠: 原 {@code tripSpuOrderChange} 中间的 spuOrderId 参数删除,
     * 子层唯一为 sku_order</p>
     *
     * @param orderNoList 交易单ID列表, 可为 null
     * @param skuOrderNoList SKU订单ID列表, 可为 null
     * @return 状态变更结果
     */
    TripOrderChangeRes tripOrderChange(List<String> orderNoList, List<String> skuOrderNoList);

    /**
     * 获取C端用户预支付单
     * @param memberOrderCreateCommand
     * @return
     */
    OrderCreateRes getPrePayOrder(MemberOrderCreateCommand memberOrderCreateCommand);

    void deliverEdit(DeliverCodeCommand deliverCommand);

    /**
     * 订单收货地址修改 - 核心校验（整合所有前置校验逻辑，返回运费校验结果）
     *
     * @param orderAgg 订单聚合对象
     * @param shipVO
     * @return 各SPU对应的最新运费（用于对比是否变动）
     */
    Map<Long, Money> validateOrderShipChange(OrderAgg orderAgg, ShipVO shipVO);

    /**
     * 校验运费是否变动（独立封装，便于两处调用）
     *
     * <p>SpuOrder 层折叠: 原收 {@code List<SpuOrderDTO>}, 折叠后按 sku 侧 spuId 分组比对运费和
     * (运费只落同 spu 首个 sku, 分组和 = 原 spu 级运费)</p>
     *
     * @param orderAgg 订单聚合
     * @param goodsFreight 最新运费信息
     */
    void validateFreightUnchanged(OrderAgg orderAgg, Map<Long, Money> goodsFreight);

    /**
     * 数据库层修改订单收货地址（最终执行更新）
     * @param orderNo 订单ID
     * @param shipVOJson 收货地址JSON串
     */
    void updateOrderShipDb(String orderNo, ShipVO shipVOJson);


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
    OrderDTO order(Long orderId);

    /**
     * 订单聚合
     * @param orderId 交易单主键
     * @return
     */
    OrderAgg orderAgg(Long orderId);

    /**
     * 订单聚合
     * @param orderNo
     * @return
     */
    OrderAgg orderAgg(String orderNo);

    /**
     * 交易单-列表
     * @param orderQuery
     * @return
     */
    Page<OrderVO> orderPage(OrderQuery orderQuery);

    /**
     * 批量修改订单状态
     *
     * @param orderNoList
     * @param sourceState
     * @param toState
     * @param orderExt 订单拓展信息, 可为 null
     */
    void batchUpdateOrderState(List<String> orderNoList, OrderEnum.State sourceState, OrderEnum.State toState, OrderExt orderExt);

    List<OrderDTO> listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State orderState, LocalDateTime updateTime);

    void orderEdit(OrderDTO orderEdit);
    /**
     * 交易单发货信息
     * @param orderNo 交易单ID
     * @return
     */
    Map<Long, List<DeliverVO>> orderDeliverInfo(String orderNo);

    List<OrderItemExcelVO> queryOrderItemExcelVO(OrderQuery orderQuery);

    /**
     * 新增订单状态记录（含领域规则校验）
     * @param entity 订单状态记录领域模型
     * @return 新增后的领域模型
     */
    OrderStateRecordEntity createStateRecord(OrderStateRecordEntity entity);

    Page<OrderStateRecordVO> recordPage(OrderStateRecordQuery query);

    /**
     * 按 spuOrderId 查询订单状态记录列表(按操作时间倒序)
     *
     * @param orderNo 商品订单 ID
     * @return 订单状态记录列表
     */
    List<OrderStateRecordVO> recordListByOrderNo(String orderNo);

    /**
     * 创建订单
     * @param data
     * @param orderCreateCommand
     * @return
     */
    OrderCreateRes createOrder(OrderGoodsCheckV2Res data, OrderCreateCommand orderCreateCommand);

    /**
     * 保存预支付单
     * @param order
     * @param memberOrderCreateCommand
     */
    void savePrePayOrder(OrderCreateRes order, MemberOrderCreateCommand memberOrderCreateCommand);
}
