package com.newzkl.platform.base.biz.goods.application.goods.service.relation;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 市场关系rpc
 * @date 2024/1/216:00
 */
public interface IRelationService {
    /**
     * 商品选品渠道商ID集合
     *
     * @param spuId
     * @return
     */
    List<Long> spuChannelRelation(Long spuId);
}
