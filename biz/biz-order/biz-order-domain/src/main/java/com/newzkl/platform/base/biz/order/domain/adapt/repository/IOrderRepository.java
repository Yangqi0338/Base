package com.newzkl.platform.base.biz.order.domain.adapt.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderStateVO;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.req.query.*;
import com.newzkl.platform.base.biz.order.model.res.AlreadyDeliverRes;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.dto.OrderStateCheckDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuRefundDTO;

import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.common.ddd.model.enums.SettleType;
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
     * 订单-实体
     * @param orderId
     * @return
     */
    OrderDTO order(Long orderId);
    /**
     * 订单-列表
     * @param orderQuery
     * @return
     */
    Page<OrderDTO> orderList(OrderQuery orderQuery);
    /**
     * SPU订单-列表
     * @param spuOrderQuery
     * @return
     */
    Page<SpuOrderDTO> spuOrderList(SpuOrderQuery spuOrderQuery);

    /**
     * SKU订单列表
     * @param orderQuery
     * @return
     */
    Page<SkuOrderDTO> skuOrderList(SkuOrderQuery orderQuery);

    int updateSkuRefundingCount(Long orderId, Long skuId, Integer count);

    int updateSpuRefundingCount(Long spuOrderId, Integer count);

    List<SkuRefundDTO> skuRefundResList(Long orderId, List<Long> skuIds);

    /**
     * spu订单状态数量count
     *
     * @param spuOrderQuery
     * @return
     */
    Map<OrderEnum.State, Integer> stateCountMap(SpuOrderQuery spuOrderQuery);

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
    int batchUpdateSpuOrderState(List<Long> spuOrderIdList,  OrderEnum.State sourceState,  OrderEnum.State toState);
    int batchUpdateSpuOrderStateByOrderId(List<Long> orderIdList,  OrderEnum.State sourceState,  OrderEnum.State toState,String spuOrderExt);
    /**
     * 批量修改Sku订单状态
     * @param skuOrderIdList
     * @param sourceState
     * @param toState
     * @param skuOrderCommand
     */
    int batchUpdateSkuOrderState(List<Long> skuOrderIdList, OrderEnum.State sourceState, OrderEnum.State toState, SkuOrderCommand skuOrderCommand);
    int batchUpdateSkuOrderStateByOrderId(List<Long> orderIdList, OrderEnum.State sourceState, OrderEnum.State toState);
    /**
     * 订单ID-列表
     * @param orderQuery
     * @return
     */
    List<Long> orderIdList(OrderQuery orderQuery);

    List<SpuOrderStateVO> accountOrderState(Long accountId, List<Long> orderIdList);

    SpuOrderRelationVO spuOrderRelation(Long orderId, Long spuId);

    List<OrderStateCheckDTO> checkSpuOrderState(List<Long> orderId);

    List<OrderStateCheckDTO> checkOrderState(List<Long> orderId);

    List<Long> orderIdBySpuSkuOrderId(List<Long> spuOrderId, List<Long> skuOrderId);

    int skuOrderEditForRefundPass(List<Long> skuIdList);

    void orderStateNotify(OrderEnum.OrderType orderType, Long channelId, String outOrderNo, OrderEnum.State currentState, OrderEnum.State toState);

    int skuOrderEditForRefundClose(List<Long> idList);

    int cutSkuOrderRefundingNumber(Long spuOrderId, List<Long> skuIdList);

    int skuOrderSave(SkuOrderDTO sku, SkuOrderQuery skuQuery);

    int skuOrderSave(List<SkuOrderDTO> spuList);

    int spuOrderSave(SpuOrderDTO spu, SpuOrderQuery spuOrderQuery);

    int spuOrderSave(List<SpuOrderDTO> spuList);

    List<SettlementConfigOutVO> settlementConfigBatch(List<Long> supplierIdList);

    EarningsConfigRpcVO channelEarningsConfig(Long channelId);

    List<AlreadyDeliverRes> getAlreadyDeliverResList(Long spuOrderId, List<Long> skuIds);

    boolean deliverSave(Deliver deliver);

    int updateSkuDeliverCount(DeliverItemVO deliverItem);

    List<Long> querySkuOrderIdList(Long spuOrderId, List<Long> skuIdList);

    SpuOrderDTO spuOrder(Long spuOrderId);

    boolean orderSave(OrderDTO orderEdit);

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

    void deliverDelete(Long l);

    // FIXME[outorder-removed]: 三方履约订单持久化(OutOrderDO/DAO)已删, 此声明待后期以新履约模型替换
    // void outOrderSave(List<OutOrder> outOrderList);

    List<SkuOrderVO> querySkuOrderByOrderId(Long orderId, List<Long> skuIdList);

    SettleType settleOrderType(Long supplierId);

    void updateOrderShip(Long orderId, ShipVO shipVo);

    void updateSpuOrderShip(Long orderId, ShipVO shipVo);

    List<SpuOrderDTO> listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State orderState, LocalDateTime updateTime);

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
    Money spuOrderSumAmount(SpuOrderQuery spuOrderQuery);

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

    List<DeliverVO> deliverListByQuery(DeliverQuery deliverQuery);

    /**
     * 保存/更新订单状态记录
     */
    OrderStateRecordEntity createStateRecord(OrderStateRecordEntity record);

    Page<OrderStateRecordEntity> recordPage(OrderStateRecordQuery query);
}