package com.newzkl.platform.base.biz.order.application.service;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateVO;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SpuOrderQuery;
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
     * SPU订单聚合值对象
     * @param spuOrderId
     * @return
     */
    SpuOrderAggVO spuOrderAggVO(Long spuOrderId);
    /**
     * SPU聚合订单分页
     * @param spuOrderQuery
     * @return
     */
    Page<SpuOrderAggVO> spuOrderAggVOList(SpuOrderQuery spuOrderQuery);
    /**
     * SPU订单用户关系信息
     *
     * @param orderId
     * @param spuId
     * @return
     */
    SpuOrderRelationVO spuOrderRelation(Long orderId, Long spuId);
    /**
     * 用户订单状态
     * @param accountId
     * @param spuOrderIdList
     * @return
     */
    List<OrderStateVO> accountOrderState(Long accountId, List<Long> spuOrderIdList);
    /**
     * 订单ID列表
     * @param orderQuery
     * @return
     */
    List<Long> orderIdList(OrderQuery orderQuery);
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

    Map<Long, List<DeliverVO>> orderDeliverInfo(Long spuOrderId);
}
