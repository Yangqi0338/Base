package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.biz.order.model.support.api.order.GoodsVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckReq;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckRes;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckV2Res;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;

import java.util.List;

/**
 * 下单商品校验出站端口
 *
 * <p>迁移: 原 domain 直连 {@code @DubboReference IOrderGoodsFacade} 违依赖硬线,
 * 抽为出站端口, 由 infra 实现(远程调 biz-goods 域校验商品状态/库存/运费)
 *
 * @author KC
 */
public interface OrderGoodsCheckApi {

    /**
     * 校验下单商品(V2)
     *
     * @param checkReq  校验请求(渠道/门店/收货地址)
     * @param goodsList 下单商品列表
     * @return 校验结果, 含拆单后的商品分布信息
     */
    PlatformResult<OrderGoodsCheckV2Res> orderGoodsCheckV2(OrderGoodsCheckReq checkReq, List<GoodsVO> goodsList);

    /**
     * 校验下单商品并返回运费(改地址复用)
     *
     * @param checkReq  校验请求(渠道/门店/收货地址)
     * @param goodsList 下单商品列表
     * @return 校验结果, 含各 SPU 最新运费
     */
    PlatformResult<OrderGoodsCheckRes> checkShip(OrderGoodsCheckReq checkReq, List<GoodsVO> goodsList);
}
