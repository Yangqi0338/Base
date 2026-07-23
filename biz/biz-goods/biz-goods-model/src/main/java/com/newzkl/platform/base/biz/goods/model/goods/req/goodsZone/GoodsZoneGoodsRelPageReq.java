package com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneGoodsRelRes;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分组-商品关联分页查询请求
 * @author sijiwang
 * @since 2026-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsZoneGoodsRelPageReq extends Page<GoodsZoneGoodsRelRes> {

    /**
     * 分组ID
     */
    @NotNull(message = "分组ID不能为空")
    private Long groupId;

    /**
     * 商品ID
     */
    private Long spuId;
}