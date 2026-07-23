package com.newzkl.platform.base.biz.goods.domain.goodsZone.convert;


import com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe.GoodsZoneGoodsRel;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneGoodsRelRes;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品关联转换工具类
 * @author sijiwang
 * @since 2026-03-18
 */
public class GoodsZoneGoodsRelConvertUtil {

    /**
     * 领域实体 → 响应VO
     * @param rel 领域实体
     * @return 响应VO
     */
    public static GoodsZoneGoodsRelRes convertToVO(GoodsZoneGoodsRel rel) {
        if (rel == null) {
            return null;
        }

        GoodsZoneGoodsRelRes res = new GoodsZoneGoodsRelRes();
        res.setId(rel.getId());
        res.setGroupId(rel.getGroupId());
        res.setSpuId(rel.getSpuId());
        res.setSpu(rel.getSpu());
        res.setCreateTime(rel.getCreateTime());

        return res;
    }

    /**
     * 领域实体列表 → 响应VO列表
     * @param relList 领域实体列表
     * @return 响应VO列表
     */
    public static List<GoodsZoneGoodsRelRes> convertToVOList(List<GoodsZoneGoodsRel> relList) {
        if (relList == null || relList.isEmpty()) {
            return new ArrayList<>();
        }

        List<GoodsZoneGoodsRelRes> resList = new ArrayList<>();
        for (GoodsZoneGoodsRel rel : relList) {
            resList.add(convertToVO(rel));
        }
        return resList;
    }
}