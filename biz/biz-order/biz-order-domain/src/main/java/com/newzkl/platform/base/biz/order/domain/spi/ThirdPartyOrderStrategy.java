package com.newzkl.platform.base.biz.order.domain.spi;


import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuCountDTO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import com.newzkl.platform.base.common.ddd.domain.StrategyNullableProcessor;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderDTO;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderResult;

import java.util.List;

/**
 * 第三方下单策略接口（封装不同第三方的下单逻辑）
 */
public interface ThirdPartyOrderStrategy extends StrategyNullableProcessor {
    /**
     * 创建第三方订单
     * 
     * @param outGoods 外部商品列表
     * @param order    系统内部订单
     * @return 适配后的第三方订单结果
     */
    ThirdPartyOrderResult create(List<OrderSkuVO> outGoods, OrderDTO order);

    ThirdPartyOrderResult delivery(String outOrderNo, List<SkuCountDTO> skuCountDTOList, String expressCompanyName, String expressNo, Long channelId);

    void compensation(ThirdPartyOrderRecordDTO request);
}