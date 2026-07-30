package com.newzkl.platform.base.biz.order.domain.service.impl;

import com.newzkl.platform.base.biz.order.domain.adapt.repository.SpuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.OrderCountDomain;
import com.newzkl.platform.base.biz.order.model.order.res.IndexCountRes;
import com.newzkl.platform.base.biz.order.model.order.util.GroupCountUtils;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易-订单统计领域服务实现
 *
 * <p>旧逻辑 ({@code QueryServiceImpl#indexCount}) 三步照搬:
 * 分组统计 → 补齐连续时间桶 → 追加全量订单量与订单金额。</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCountDomainImpl implements OrderCountDomain {

    private final SpuOrderRepository spuOrderRepository;

    @Override
    public IndexCountRes indexCount(TimeQuery timeQuery) {
        IndexCountRes indexCountRes = new IndexCountRes();
        List<GroupCountRes> groupCountRes = spuOrderRepository.orderGroupCount(timeQuery);
        indexCountRes.setGroupCountRes(GroupCountUtils.complete(groupCountRes, timeQuery));
        indexCountRes.setOrderCount(spuOrderRepository.countAll());
        indexCountRes.setOrderAmount(spuOrderRepository.sumOrderPayableAmountAll());
        return indexCountRes;
    }
}
