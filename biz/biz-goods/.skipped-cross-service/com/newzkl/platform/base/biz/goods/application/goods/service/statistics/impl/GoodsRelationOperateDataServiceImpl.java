package com.newzkl.platform.base.biz.goods.application.goods.service.statistics.impl;

import com.newzkl.platform.base.biz.goods.application.goods.service.statistics.IGoodsRelationOperateDataService;
import com.newzkl.platform.base.biz.goods.domain.market.relation.repository.IGoodsRelationRepository;
import com.newzkl.platform.base.biz.goods.rpc.model.relation.AlterChannelSelectorSellDataReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2024/4/25 15:45
 */
@Service
@RequiredArgsConstructor
public class GoodsRelationOperateDataServiceImpl implements IGoodsRelationOperateDataService {

    private final IGoodsRelationRepository goodsRelationRepository;

    @Override
    public void alterChannelSelectorSellData(List<AlterChannelSelectorSellDataReq> req) {
        goodsRelationRepository.alterChannelSelectorSellData(req);
    }
}
