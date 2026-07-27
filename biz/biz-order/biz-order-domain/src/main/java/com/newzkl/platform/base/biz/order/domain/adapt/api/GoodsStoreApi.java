package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;

import java.util.List;

/**
 * 商品门店出站端口
 *
 * @author KC
 */
public interface GoodsStoreApi {

    /**
     * 按门店ID批量查询门店信息
     *
     * @param storeIdList 门店ID列表
     * @return 门店信息列表, 恒非 null
     */
    List<StoreRPCVO> batchQueryStoreInfo(List<Long> storeIdList);

    /**
     * 上报门店客户支付
     *
     * @param command 门店客户支付入参
     */
    void storeAccountPayEvent(StoreAccountPayCommand command);
}
