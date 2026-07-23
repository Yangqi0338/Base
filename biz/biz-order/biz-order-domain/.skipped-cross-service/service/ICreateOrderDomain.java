package com.newzkl.platform.base.biz.order.domain.service;

import com.zkl.scm.finance.model.pay.res.TradeBaseRes;
import com.zkl.scm.goods.rpc.model.distribution.DistributionDetailVO;
import com.newzkl.platform.base.biz.order.model.order.req.CommitOrderPreReq;
import com.newzkl.platform.base.biz.order.model.order.req.CreateOrderReq;
import com.newzkl.platform.base.biz.order.model.order.req.PayOrderReq;
import com.newzkl.platform.base.biz.order.model.order.res.CreateOrderRes;
import com.newzkl.platform.base.biz.order.model.order.vo.GoodsFreightAggVO;
import com.zkl.scm.user.model.relation.vo.ShipAddressRpcVO;

import java.util.List;
import java.util.Map;

/**
 * @author sijiwang
 */
public interface ICreateOrderDomain {

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
     * 聚合商品运费数据
     * @param distributionDetailVOS
     * @return
     */
    Map<Long, GoodsFreightAggVO> aggregateGoodsFreightData(List<DistributionDetailVO> distributionDetailVOS);

    /**
     * 计算运费
     * @param goodsFreightAggMap
     * @param addressDetail
     * @return
     */
    Map<Long, Integer> calculateFreight(Map<Long, GoodsFreightAggVO> goodsFreightAggMap, ShipAddressRpcVO addressDetail);
}
