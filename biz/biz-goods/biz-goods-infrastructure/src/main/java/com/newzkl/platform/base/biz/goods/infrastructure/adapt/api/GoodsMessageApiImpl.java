package com.newzkl.platform.base.biz.goods.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.goods.domain.adapt.api.EventApi;
import com.newzkl.platform.base.biz.goods.model.goods.event.GoodsSaleStateEvent;
import com.newzkl.platform.base.biz.goods.model.goods.event.SkuEditEvent;
import com.newzkl.platform.base.biz.goods.model.goods.event.SpuEditEvent;
import com.newzkl.platform.base.common.core.mq.infrastructure.utils.MQUtil;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * {@link EventApi} 的基础设施实现
 *
 * <p>对齐 biz-order {@code LocalMessageApiImpl}: 经 {@code MQUtil} 把商品业务事件投到各自 tag。
 * 一个事件一个 tag 负载即事件本体不带包装字段, 订阅方按 tag 区分事件类型。
 * 拆服务时只改本 impl 为远程发送, 领域层零改动</p>
 *
 * @author KC
 */
@Slf4j
@Component("goodsMessageApi")
public class GoodsMessageApiImpl implements EventApi {

    @Override
    public void publishSpuEdit(Long spuId) {
        MQUtil.send(MQ.Tag.GOODS_SPU_EDIT_EVENT, new SpuEditEvent(spuId));
        log.info("商品业务事件已投递 tag={} spuId={}", MQ.Tag.GOODS_SPU_EDIT_EVENT, spuId);
    }

    @Override
    public void publishSkuEdit(Long spuId, List<Long> skuIdList) {
        MQUtil.send(MQ.Tag.GOODS_SKU_EDIT_EVENT, new SkuEditEvent(spuId, skuIdList));
        log.info("商品业务事件已投递 tag={} spuId={} skuIdList={}",
                MQ.Tag.GOODS_SKU_EDIT_EVENT, spuId, skuIdList);
    }

    @Override
    public void publishSkuDelete(Long spuId, List<Long> skuIdList) {
        MQUtil.send(MQ.Tag.GOODS_SKU_DELETE_EVENT, new SkuEditEvent(spuId, skuIdList));
        log.info("商品业务事件已投递 tag={} spuId={} skuIdList={}",
                MQ.Tag.GOODS_SKU_DELETE_EVENT, spuId, skuIdList);
    }

    @Override
    public void publishSaleState(List<Long> spuIdList, Integer sourceState, Integer newState) {
        if (spuIdList == null || spuIdList.isEmpty()) {
            return;
        }
        // 逐 SPU 投递: 订阅方按单个 spuId 反查收件人, 一条消息只能带自己那一个 spuId
        for (Long spuId : spuIdList) {
            MQUtil.send(MQ.Tag.GOODS_SPU_STATE_EVENT,
                    new GoodsSaleStateEvent(Collections.singletonList(spuId), sourceState, newState));
            log.info("商品业务事件已投递 tag={} spuId={} sourceState={} newState={}",
                    MQ.Tag.GOODS_SPU_STATE_EVENT, spuId, sourceState, newState);
        }
    }

    @Override
    public void publishSkuPrice(Long spuId, List<Long> skuIdList) {
        MQUtil.send(MQ.Tag.GOODS_SKU_PRICE_EVENT, new SkuEditEvent(spuId, skuIdList));
        log.info("商品业务事件已投递 tag={} spuId={} skuIdList={}",
                MQ.Tag.GOODS_SKU_PRICE_EVENT, spuId, skuIdList);
    }
}
