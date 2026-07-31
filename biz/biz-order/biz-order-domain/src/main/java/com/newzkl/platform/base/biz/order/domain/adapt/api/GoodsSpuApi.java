package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiSkuVO;
import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiSpuVO;

import java.util.List;

/**
 * 商品 SPU 出站端口
 *
 * @author KC
 */
public interface GoodsSpuApi {

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
}
