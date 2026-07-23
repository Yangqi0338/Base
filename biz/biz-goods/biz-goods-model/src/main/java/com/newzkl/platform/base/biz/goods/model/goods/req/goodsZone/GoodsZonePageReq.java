package com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分组分页查询请求
 * @author sijiwang
 * @since 2026-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsZonePageReq extends Page<GoodsZoneRes> {

    /**
     * 分组名称（模糊查询）
     */
    private String groupName;

    /**
     * 分组状态：0-禁用 1-启用
     */
    private Integer state;
}