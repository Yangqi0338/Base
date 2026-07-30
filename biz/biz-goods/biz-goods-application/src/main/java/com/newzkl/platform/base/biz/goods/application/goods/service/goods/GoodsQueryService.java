package com.newzkl.platform.base.biz.goods.application.goods.service.goods;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;

/**
 * 商品查询应用服务
 *
 * <p>迁移调整: 原 {@code GoodsQueryService} 聚合了选品市场 (MarketSpuVO)、汇订货 (HuiDingHuo)、
 * openapi/rpc 等跨服务查询能力, 这些方法依赖 biz-market 与外部 rpc 契约, 不属于纯商品域范畴,
 * 已在本次迁移中剥离。此处仅保留商品域内被 {@code IWorktableProcessor} 依赖的 {@code spuVO} 查询,
 * 其余跨服务查询待 biz-market / openapi 模块建立后再行补齐。</p>
 *
 * @author muc_fang
 */
public interface GoodsQueryService {

    /**
     * 查询 spu 详情
     *
     * @param spuId spu 主键
     * @return spu 视图对象
     */
    default SpuVO spuVO(Long spuId) {
        return spuVO(spuId, false);
    }

    /**
     * 查询 spu 详情
     *
     * @param spuId         spu 主键
     * @param needExtraInfo 是否需要额外信息
     * @return spu 视图对象
     */
    SpuVO spuVO(Long spuId, Boolean needExtraInfo);
}
