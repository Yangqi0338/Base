package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.biz.order.model.support.api.DistributionDetailVO;
import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;
import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiSkuVO;
import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiSpuVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.GoodsVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckReq;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckRes;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckV2Res;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;

import java.util.List;

/**
 * 商品 SPU 出站端口
 *
 * @author KC
 */
public interface GoodsApi {

    List<ApiSkuVO> querySkuIdListByOutId(List<String> skuIdList);

    /**
     * 按 SPU_ID 批量查询商品信息(含渠道类型)
     *
     * <p>迁移: 原 domain 直连 {@code @DubboReference IGoodsSpuFacade.apiSpuVOList} 违依赖硬线,
     * 抽为出站端口, 由 infra 实现(远程调 biz-goods 域)
     *
     * @param accountId 账户ID(可空, 无渠道上下文时传 null)
     * @param spuIdList SPU_ID 列表
     * @return 商品信息列表
     */
    List<ApiSpuVO> apiSpuVOList(Long accountId, List<Long> spuIdList);

    /**
     * 按铺货ID批量查询铺货详情
     *
     * @param distributionIds 铺货ID列表
     * @return 铺货详情列表, 恒非 null
     */
    List<DistributionDetailVO> queryDistributionDetailByIds(List<Long> distributionIds);

    /**
     * 校验下单商品(V2)
     *
     * @param checkReq  校验请求(渠道/门店/收货地址)
     * @param goodsList 下单商品列表
     * @return 校验结果, 含拆单后的商品分布信息
     */
    PlatformResult<OrderGoodsCheckV2Res> orderCheck(OrderGoodsCheckReq checkReq, List<GoodsVO> goodsList);

    /**
     * 校验下单商品并返回运费(改地址复用)
     *
     * @param checkReq  校验请求(渠道/门店/收货地址)
     * @param goodsList 下单商品列表
     * @return 校验结果, 含各 SPU 最新运费
     */
    PlatformResult<OrderGoodsCheckRes> checkShip(OrderGoodsCheckReq checkReq, List<GoodsVO> goodsList);

    /**
     * 按门店ID批量查询门店信息
     *
     * @param storeIdList 门店ID列表
     * @return 门店信息列表, 恒非 null
     */
    List<StoreRPCVO> batchQueryStoreInfo(List<Long> storeIdList);
}
