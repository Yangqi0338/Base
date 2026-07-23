package com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 商品分组-商品关联新增请求
 * @author sijiwang
 * @since 2026-03-18
 */
@Data
public class GoodsZoneGoodsRelAddReq {

    /**
     * 分组ID
     */
    @NotNull(message = "分组ID不能为空")
    private Long groupId;

    /**
     * 商品ID列表
     */
    @NotEmpty(message = "商品ID列表不能为空")
    private List<Long> spuList;

}