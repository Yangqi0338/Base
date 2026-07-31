package com.newzkl.platform.base.biz.order.application.service;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderStateVO;
import com.newzkl.platform.base.biz.order.model.dto.IndexCountRes;
import com.newzkl.platform.base.biz.order.model.req.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.req.SpuOrderQuery;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;

import java.util.List;
import java.util.Map;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/813:55
 */
public interface IQueryService {
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
     * 订单聚合值对象
     * @param orderId
     * @return
     */
    OrderAggVO orderAggVO(Long orderId);
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
    List<SpuOrderStateVO> accountOrderState(Long accountId, List<Long> spuOrderIdList);
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
}
