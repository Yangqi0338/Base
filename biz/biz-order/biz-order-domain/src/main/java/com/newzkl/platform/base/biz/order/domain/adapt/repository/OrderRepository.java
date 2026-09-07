package com.newzkl.platform.base.biz.order.domain.adapt.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateVO;
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
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
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
public interface OrderRepository {
    /**
     * 订单-实体
     * @param orderId
     * @return
     */
    OrderDTO order(Long orderId);
    /**
     * 订单-实体
     * @param orderNo
     * @return
     */
    OrderDTO order(String orderNo);
    /**
     * 订单-列表
     * @param orderQuery
     * @return
     */
    Page<OrderDTO> orderList(OrderQuery orderQuery);
    /**
     * SKU订单列表
     * @param orderQuery
     * @return
     */
    Page<SkuOrderDTO> skuOrderList(SkuOrderQuery orderQuery);

    int updateSkuRefundingCount(String orderNo, Long skuId, Integer count);

    List<SkuRefundDTO> skuRefundResList(String orderNo, List<Long> skuIds);

    /**
     * 交易单状态数量count
     *
     * @param orderQuery 查询条件
     * @return 状态-数量映射
     */
    Map<OrderEnum.State, Integer> stateCountMap(OrderQuery orderQuery);

    /**
     * 交易单聚合值对象
     * @param orderNO 交易单号
     * @return 交易单聚合
     */
    OrderAggVO orderAggVO(String orderNO);
    /**
     * 批量修改订单状态
     *
     * <p>SpuOrder 层折叠: 原 spu_order_ext 写入合并进本方法, orderExt 为空则不更新该列</p>
     *
     * @param orderNoList 交易单ID列表
     * @param sourceState 原状态
     * @param toState 目标状态
     * @param orderExt 订单拓展信息, 可为 null
     * @return 影响行数
     */
    int batchUpdateOrderState(List<String> orderNoList, OrderEnum.State sourceState, OrderEnum.State toState, OrderExt orderExt);
    /**
     * 批量修改Sku订单状态
     * @param skuOrderNoList
     * @param sourceState
     * @param toState
     * @param skuOrderCommand
     */
    int batchUpdateSkuOrderState(List<String> skuOrderNoList, OrderEnum.State sourceState, OrderEnum.State toState, SkuOrderCommand skuOrderCommand);
    int batchUpdateSkuOrderState(List<String> orderNoList, OrderEnum.State sourceState, OrderEnum.State toState);
    /**
     * 订单ID-列表
     * @param orderQuery
     * @return
     */
    List<Long> orderIdList(OrderQuery orderQuery);

    List<OrderStateVO> accountOrderState(Long accountId, List<Long> orderIdList);

    OrderRelationVO orderRelation(Long orderId, Long spuId);

    List<OrderStateCheckDTO> checkOrderState(List<String> orderNoList);

    List<String> orderNoBySkuOrderNo(List<String> skuOrderNoList);

    int skuOrderEditForRefundPass(List<String> skuOrderNoList);

    int skuOrderEditForRefundClose(List<String> skuOrderNoList);

    int skuOrderSave(SkuOrderDTO sku, SkuOrderQuery skuQuery);

    int skuOrderSave(List<SkuOrderDTO> spuList);

    List<SettlementConfigOutVO> settlementConfigBatch(List<Long> supplierIdList);

    EarningsConfigRpcVO channelEarningsConfig(Long channelId);

    List<AlreadyDeliverRes> getAlreadyDeliverResList(String orderNo, List<Long> skuIds);

    boolean deliverSave(Deliver deliver);

    /**
     * 累加SKU发货数量
     *
     * @param orderNo 交易单号
     * @param deliverItem 发货明细
     * @return 影响行数
     */
    int updateSkuDeliverCount(String orderNo, DeliverItemVO deliverItem);

    List<Long> querySkuOrderIdList(String orderNo, List<Long> skuIdList);

    List<String> querySkuOrderNoList(String orderNo, List<Long> skuIdList);

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

    EarningsEnum.SettleType settleOrderType(Long supplierId);

    void updateOrderShip(String orderNo, com.newzkl.platform.base.common.ddd.model.vo.ShipVO shipVo);

    List<OrderDTO> listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State orderState, LocalDateTime updateTime);

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
     * 交易单数量统计
     *
     * @param orderQuery 查询条件
     * @return 订单数量
     */
    Integer orderCount(OrderQuery orderQuery);

    /**
     * 交易单金额求和
     *
     * @param orderQuery 查询条件
     * @return 订单金额合计
     */
    Money orderSumAmount(OrderQuery orderQuery);

    /**
     * 按交易单ID查物流列表
     *
     * @param orderNo 交易单ID(物流表列名 spu_order_id 存 orderId 值)
     * @return 物流列表
     */
    List<DeliverVO> deliverListByOrderNo(String orderNo);

    List<DeliverVO> deliverListByQuery(DeliverQuery deliverQuery);

    /**
     * 保存/更新订单状态记录
     */
    OrderStateRecordEntity createStateRecord(OrderStateRecordEntity record);

    Page<OrderStateRecordEntity> recordPage(OrderStateRecordQuery query);

    List<String> orderNoList(OrderQuery orderQuery);
}