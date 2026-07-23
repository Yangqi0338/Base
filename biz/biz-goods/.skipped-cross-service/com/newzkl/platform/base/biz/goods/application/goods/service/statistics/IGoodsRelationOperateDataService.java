package com.newzkl.platform.base.biz.goods.application.goods.service.statistics;


import com.newzkl.platform.base.biz.goods.rpc.model.relation.AlterChannelSelectorSellDataReq;

import java.util.List;

/**
 * @author niu
 * @description: 商品关系运营数据接口
 * @date 2024/4/25 15:45
 */
public interface IGoodsRelationOperateDataService {

    /**
     * 更新渠道商选品销售数据
     * @param req
     */
    void alterChannelSelectorSellData(List<AlterChannelSelectorSellDataReq> req);
}
