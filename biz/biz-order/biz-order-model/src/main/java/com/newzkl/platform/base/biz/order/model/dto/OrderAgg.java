package com.newzkl.platform.base.biz.order.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 交易单聚合
 * @date 2023/11/289:35
 */
@Data
public class OrderAgg implements Serializable {

    /**
     * 是否是初始化
     */
    private boolean isInit = true;
    /**
     * 交易单
     */
    private Order order;
    /**
     * spu订单
     */
    private List<SpuOrder> spuOrderList;
    /**
     * sku订单
     */
    private List<SkuOrder> skuOrderList;

    // 迁移说明: OrderAgg 退化为纯数据聚合(贫血模型), 原聚合根支付/建单业务方法
    // (channelPaySuccess/memberPaySuccess/directPaySuccess/allPaySuccess/buildCreateOrder 等)已按本团队方言
    // 平展到 application/domain service(去聚合根)。channelPaySuccess/allPaySuccess 现落 OrderServiceImpl;
    // directPaySuccess/buildCreateOrder 全仓无调用方, 剥离(登 deferred, 用时在 application/domain 平展重建)。
}
