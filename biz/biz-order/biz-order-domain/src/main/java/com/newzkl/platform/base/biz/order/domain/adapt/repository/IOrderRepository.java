package com.newzkl.platform.base.biz.order.domain.adapt.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderStateVO;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.res.AlreadyDeliverRes;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.res.OrderStateCheckRes;
import com.newzkl.platform.base.biz.order.model.support.api.ChannelNowServiceFeeRes;
import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;
import com.newzkl.platform.base.biz.order.model.support.api.SettlementConfigOutVO;
import com.newzkl.platform.base.biz.order.model.support.api.SkuSaleInfo;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* 订单
* @author fang
*/
public interface IOrderRepository {
    /**
     * 订单聚合
     * @param orderId
     * @return
     */
    OrderAgg orderAgg(Long orderId);
    /**
     * 订单-实体
     * @param orderId
     * @return
     */
    Order order(Long orderId);
    /**
     * 订单-值对象
     * @param orderId
     * @return
     */
    OrderVO orderVO(Long orderId);
    /**
     * 订单-列表
     * @param orderQuery
     * @return
     */
    Page<OrderVO> orderVOList(OrderQuery orderQuery);
    /**
     * SPU订单-列表
     * @param spuOrderQuery
     * @return
     */
    Page<SpuOrderVO> spuOrderVOList(SpuOrderQuery spuOrderQuery);

    /**
     * 订单聚合修改
     *
     * @param orderAgg
     */
    void orderAggUpdate(OrderAgg orderAgg);

    /**
     * SKU订单列表
     * @param orderQuery
     * @return
     */
    Page<SkuOrderVO> skuOrderVOList(SkuOrderQuery orderQuery);

    /**
     * spu订单状态数量count
     *
     * @param spuOrderQuery
     * @return
     */
    Map<Integer, Integer> stateCountMap(SpuOrderQuery spuOrderQuery);

    /**
     * 订单聚合保存
     * @param orderAgg
     */
    void orderAggSave(OrderAgg orderAgg);
    /**
     * SPU订单值对象
     * @param spuOrderId
     * @return
     */
    SpuOrderVO spuOrderVO(Long spuOrderId);
    /**
     * SPU订单聚合值对象
     * @param spuOrderId
     * @return
     */
    SpuOrderAggVO spuOrderAggVO(Long spuOrderId);
    /**
     * 批量修改订单状态
     * @param orderIdList
     * @param sourceState
     * @param toState
     * @return
     */
    int batchUpdateOrderState(List<Long> orderIdList,  OrderEnum.State sourceState,  OrderEnum.State toState);
    /**
     * 批量修改Spu订单状态
     * @param spuOrderIdList
     * @param sourceState
     * @param toState
     */
    void batchUpdateSpuOrderState(List<Long> spuOrderIdList,  OrderEnum.State sourceState,  OrderEnum.State toState);
    void batchUpdateSpuOrderStateByOrderId(List<Long> orderIdList,  OrderEnum.State sourceState,  OrderEnum.State toState,String spuOrderExt);
    /**
     * 批量修改Sku订单状态
     * @param skuOrderIdList
     * @param sourceState
     * @param toState
     * @param skuOrderCommand
     */
    int batchUpdateSkuOrderState(List<Long> skuOrderIdList, OrderEnum.State sourceState, OrderEnum.State toState, SkuOrderCommand skuOrderCommand);
    void batchUpdateSkuOrderStateByOrderId(List<Long> orderIdList, OrderEnum.State sourceState, OrderEnum.State toState);
    /**
     * 订单ID-列表
     * @param orderQuery
     * @return
     */
    List<Long> orderIdList(OrderQuery orderQuery);

    List<SpuOrderStateVO> accountOrderState(Long accountId, List<Long> orderIdList);

    SpuOrderRelationVO spuOrderRelation(Long orderId, Long spuId);

    List<OrderStateCheckRes> checkSpuOrderState(List<Long> orderId);

    OrderAggVO orderAggVO(Long orderId);

    List<OrderStateCheckRes> checkOrderState(List<Long> orderId);

    List<Long> orderIdBySpuSkuOrderId(List<Long> spuOrderId, List<Long> skuOrderId);

    void skuOrderEditForRefundPass(List<Long> skuIdList);

    void orderStateNotify(OrderEnum.OrderType orderType, Long channelId, String outOrderNo, OrderEnum.State currentState, OrderEnum.State toState);

    void skuOrderEditForRefundClose(List<Long> skuIdList);

    /**
     * 查询渠道商当前服务费
     * @param channelId
     * @return
     */
    ChannelNowServiceFeeRes queryChannelNowServiceFee(Long channelId);

    void skuOrderEditByQuery(SkuOrder sku, SkuOrderQuery skuQuery);

    void spuOrderEditByQuery(SpuOrder spu, SpuOrderQuery spuOrderQuery);

    void wakeUpDelayMessage(String key);

    /**
     * 唤醒分润延迟消息
     *
     * <p>迁移: 原 domain 拼 core-mq 常量 Tag.EARNING + skuOrderId, tag 拼接下沉本方法(infra 实现)
     *
     * @param skuOrderId SKU订单ID
     */
    void wakeUpEarningMessage(Long skuOrderId);

    List<SettlementConfigOutVO> settlementConfigBatch(List<Long> supplierIdList);

    EarningsConfigRpcVO channelEarningsConfig(Long channelId);

    List<AlreadyDeliverRes> getAlreadyDeliverResList(Long spuOrderId, List<Long> skuIds);

    void deliverSave(Deliver deliver);

    List<Long> querySkuOrderIdList(Long spuOrderId, List<Long> skuIdList);

    SpuOrder spuOrder(Long spuOrderId);

    void orderEdit(Order orderEdit);

    /**
     * 保存预支付单
     * @param order
     * @param memberOrderCreateCommand
     */
    void savePrePayOrder(OrderCreateRes order, MemberOrderCreateCommand memberOrderCreateCommand);

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

    /**
     * 获取C端用户预支付单的过期时间
     * @param memberOrderCreateCommand
     * @return
     */
    Long getPrePayOrderExpire(MemberOrderCreateCommand memberOrderCreateCommand);

    void deliverEdit(Deliver deliver);

    void deliverDelete(Long l);

    /**
     * 统计渠道商所有订单状态数量
     * @param channelId 渠道商ID
     * @return 订单状态统计列表
     */
    List<OrderStateCountVO> countOrderStateByChannel(Long channelId);

    /**
     * 统计会员所有订单状态数量
     * @param accountId 会员ID
     * @return 订单状态统计列表
     */
    List<OrderStateCountVO> countOrderStateByAccount(Long accountId);

    void outOrderSave(List<OutOrder> outOrderList);

    List<SkuOrderVO> querySkuOrderByOrderId(Long orderId, List<Long> skuIdList);

    void deliverNotify(String outOrderNo, List<SkuCountDTO> skuCountDTOList, String expressCompanyName, String expressNo, Long channelId);

    RoleEnum.OrderType settleOrderType(Long supplierId);

    void updateOrderShip(Long orderId, String shipVo);

    List<SpuOrder> listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State orderState, LocalDateTime updateTime);

    /**
     * 门店用户支付消息
     */
    void storeAccountPay(Long storeId, Long accountId, Integer memberAmount);

    void sendOrderNewRecordEvent(List<SpuOrder> spuOrderList, OrderEnum.State beforeOrderState, OrderEnum.State afterOrderState, Long operatorId, RoleEnum.CompanyRole operatorRoleId);

    /**
     * 订单交易额时间切片统计(已补全空白区间)
     *
     * <p>迁移: 原 domain 直连 spuOrderDAO.orderCount + ScmUtil.groupCountRes2Complete(依赖 new-scm),
     * 切片补全下沉 infra
     *
     * @param timeQuery 时间查询(起止/分组类型/分组数)
     * @return 按时间切片补全后的统计列表
     */
    List<GroupCountRes> orderCountComplete(TimeQuery timeQuery);

    /**
     * SPU订单数量统计
     *
     * @param spuOrderQuery 查询条件
     * @return 订单数量
     */
    Integer spuOrderCount(SpuOrderQuery spuOrderQuery);

    /**
     * SPU订单金额求和
     *
     * @param spuOrderQuery 查询条件
     * @return 订单金额合计
     */
    Integer spuOrderSumAmount(SpuOrderQuery spuOrderQuery);

    /**
     * 按SPU订单ID查物流列表
     *
     * @param spuOrderId SPU订单ID
     * @return 物流列表
     */
    List<DeliverVO> deliverListBySpuOrderId(Long spuOrderId);

    /**
     * 按交易单ID+SKU查SPU订单ID
     *
     * @param orderId 交易单ID
     * @param skuId   SKU_ID
     * @return SPU订单ID
     */
    Long spuOrderIdByOrderSku(Long orderId, Long skuId);

    /**
     * 查询SPU订单明细导出数据
     *
     * @param spuOrderQuery 查询条件
     * @return 导出明细列表
     */
    List<SpuOrderItemExcelVO> querySpuOrderItemExcelVO(SpuOrderQuery spuOrderQuery);
}