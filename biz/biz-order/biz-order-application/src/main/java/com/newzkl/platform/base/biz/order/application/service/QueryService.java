package com.newzkl.platform.base.biz.order.application.service;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateVO;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/813:55
 */
public interface QueryService {
    /**
     * 交易单聚合值对象
     * @param orderNo
     * @return
     */
    OrderAggVO orderAggVO(String orderNo);
    /**
     * 交易单聚合分页
     * @param orderQuery
     * @return
     */
    Page<OrderAggVO> orderAggVOList(OrderQuery orderQuery);
    /**
     * 交易单用户关系信息
     *
     * @param orderId
     * @param spuId
     * @return
     */
    OrderRelationVO orderRelation(Long orderId, Long spuId);
    /**
     * 用户订单状态
     * @param accountId
     * @param orderIdList
     * @return
     */
    List<OrderStateVO> accountOrderState(Long accountId, List<Long> orderIdList);
    /**
     * 订单ID列表
     * @param orderQuery
     * @return
     */
    List<Long> orderIdList(OrderQuery orderQuery);
    List<String> orderNoList(OrderQuery orderQuery);
    /**
     * 订单列表
     * @param orderQuery
     * @return
     */
    Page<OrderVO> orderVOList(OrderQuery orderQuery);
    /**
     * SKU订单列表
     * @param orderQuery
     * @return
     */
    Page<SkuOrderVO> skuOrderVOList(SkuOrderQuery orderQuery);

    Map<Long, List<DeliverVO>> orderDeliverInfo(String orderNo);
}
