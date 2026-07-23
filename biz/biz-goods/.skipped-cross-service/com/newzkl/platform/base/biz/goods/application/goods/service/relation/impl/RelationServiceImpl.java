package com.newzkl.platform.base.biz.goods.application.goods.service.relation.impl;

import cn.hutool.core.util.ObjectUtil;
import com.newzkl.platform.base.biz.goods.application.goods.service.relation.IRelationService;
import com.newzkl.platform.base.biz.goods.domain.market.relation.service.IGoodsRelationDomain;
import com.newzkl.platform.base.biz.goods.model.market.dto.relation.GoodsRelationQueryDTO;
import com.newzkl.platform.base.biz.goods.model.market.dto.relation.MarketGoodsRelationDTO;
import com.newzkl.platform.base.biz.goods.model.enums.goods.GoodsRelationEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/11/2816:19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RelationServiceImpl implements IRelationService {

    private final IGoodsRelationDomain goodsRelationDomain;

    @Override
    public List<Long> spuChannelRelation(Long spuId) {
        GoodsRelationQueryDTO queryGoodsListReq = new GoodsRelationQueryDTO();
        queryGoodsListReq.setRelationType(GoodsRelationEnum.GoodsRelation.SELECT_GOODS.getRelationType());
        queryGoodsListReq.setGoodsIdList(Collections.singletonList(spuId));
        List<MarketGoodsRelationDTO> marketGoodsRelationVOList = goodsRelationDomain.queryGoodsRelationListByDTO(queryGoodsListReq);
        if (ObjectUtil.isEmpty(marketGoodsRelationVOList)) {
            return new ArrayList<>();
        }
        return marketGoodsRelationVOList.stream().map(MarketGoodsRelationDTO::getUserId).distinct().collect(Collectors.toList());
    }

}
