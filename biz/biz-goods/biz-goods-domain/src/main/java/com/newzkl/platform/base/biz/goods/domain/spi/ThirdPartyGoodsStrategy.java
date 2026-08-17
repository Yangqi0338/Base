package com.newzkl.platform.base.biz.goods.domain.spi;

import com.newzkl.platform.base.common.ddd.domain.StrategyNullableProcessor;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyGoodsResult;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;

/**
 * 第三方商品同步策略接口(封装不同第三方的商品同步逻辑)
 */
public interface ThirdPartyGoodsStrategy extends StrategyNullableProcessor {

    /**
     * 同步一个第三方商品
     *
     * <p>biz-goods 不识别各三方的商品结构 只传 platformType + 外部SPU + 商品数据 JSON
     * 由命中的策略(supports(platformType))自行反序列化落库</p>
     *
     * @param platformType 平台类型
     * @param outSpuId     外部商品(SPU)ID
     * @param itemJson     第三方推送的商品数据 JSON
     * @return 同步结果适配
     */
    ThirdPartyGoodsResult sync(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String outSpuId, String itemJson);
}
