package com.newzkl.platform.base.biz.goods.infrastructure.adapt.api;

import com.alibaba.fastjson2.JSON;
import com.newzkl.platform.base.biz.goods.domain.adapt.api.GoodsMessageApi;
import com.newzkl.platform.base.biz.goods.model.enums.NotifyEnums;
import com.newzkl.platform.base.biz.goods.model.goods.event.ApiGoodsSaleStateEvent;
import com.newzkl.platform.base.biz.goods.model.goods.event.ApiSkuEditEvent;
import com.newzkl.platform.base.biz.goods.model.goods.event.ApiSpuEditEvent;
import com.newzkl.platform.base.biz.goods.model.goods.event.GoodsDeveloperNotifyMq;
import com.newzkl.platform.base.common.core.mq.infrastructure.utils.MQUtil;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@link GoodsMessageApi} 的基础设施实现
 *
 * <p>对齐 biz-order {@code LocalMessageApiImpl}: 经 {@code MQUtil} 投递商品开发者通知事件到
 * {@link MQ.Tag#GOODS_DEVELOPER_NOTIFY_EVENT}(本地消息表 + MQ)。载体只带 spuId + businessType +
 * 事件内容, 收件人由 openapi 消费方解析订阅渠道后填充。拆服务时改 impl 为远程 consumer, 领域层零改动。</p>
 *
 * @author KC
 */
@Slf4j
@Component("goodsMessageApi")
public class GoodsMessageApiImpl implements GoodsMessageApi {

    @Override
    public void notifySpuEdit(Long spuId) {
        send(spuId, NotifyEnums.GoodsType.UPDATE_SPU.getCode(), new ApiSpuEditEvent(spuId));
    }

    @Override
    public void notifySkuEdit(Long spuId, List<Long> skuIdList) {
        send(spuId, NotifyEnums.GoodsType.UPDATE_SKU.getCode(), new ApiSkuEditEvent(spuId, skuIdList));
    }

    @Override
    public void notifySkuDelete(Long spuId, List<Long> skuIdList) {
        send(spuId, NotifyEnums.GoodsType.DELETE_SKU.getCode(), new ApiSkuEditEvent(spuId, skuIdList));
    }

    @Override
    public void notifySaleState(List<Long> spuIdList, Integer sourceState, Integer newState) {
        if (spuIdList == null || spuIdList.isEmpty()) {
            return;
        }
        ApiGoodsSaleStateEvent event = new ApiGoodsSaleStateEvent(spuIdList, sourceState, newState);
        String eventContent = JSON.toJSONString(event);
        // 上下架按 SPU 逐个投递, openapi 侧逐 spuId 反查订阅渠道
        for (Long spuId : spuIdList) {
            send(spuId, NotifyEnums.GoodsType.UPDATE_spu_STATE.getCode(), eventContent);
        }
    }

    @Override
    public void notifySkuPrice(Long spuId, List<Long> skuIdList) {
        send(spuId, NotifyEnums.GoodsType.UPDATE_SPU_PRICE.getCode(), new ApiSkuEditEvent(spuId, skuIdList));
    }

    /**
     * 组装并投递商品开发者通知事件
     *
     * @param spuId SPU 主键
     * @param businessType 业务类型 code
     * @param event 事件内容对象, 序列化为 JSON
     */
    private void send(Long spuId, Integer businessType, Object event) {
        send(spuId, businessType, JSON.toJSONString(event));
    }

    /**
     * 组装并投递商品开发者通知事件
     *
     * @param spuId SPU 主键
     * @param businessType 业务类型 code
     * @param eventContent 事件内容 JSON
     */
    private void send(Long spuId, Integer businessType, String eventContent) {
        GoodsDeveloperNotifyMq mq = new GoodsDeveloperNotifyMq();
        mq.setSpuId(spuId);
        mq.setBusinessType(businessType);
        mq.setEventContent(eventContent);
        MQUtil.send(MQ.Tag.GOODS_DEVELOPER_NOTIFY_EVENT, mq);
        log.info("商品开发者通知已投递 spuId={} businessType={}", spuId, businessType);
    }
}
