package com.newzkl.platform.base.biz.goods.domain.goodsZone.convert;

import com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe.GoodsZone;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneAddReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneRes;

import java.time.LocalDateTime;

/**
 * 商品分组转换工具类
 * @author sijiwang
 * @since 2026-03-18
 */
public class GoodsZoneConvertUtil {

    /**
     * 新增/编辑请求 → 领域实体
     * @param addReq 请求参数
     * @return 领域实体
     */
    public static GoodsZone convertToEntity(GoodsZoneAddReq addReq) {
        if (addReq == null) {
            return null;
        }

        GoodsZone goodsZone = GoodsZone.builder()
                .id(addReq.getId())
                .groupName(addReq.getGroupName())
                .groupDesc(addReq.getGroupDesc())
                .backgroundImg(addReq.getBackgroundImg())
                .sortType(addReq.getSortType())
                .searchBoxStatus(addReq.getSearchBoxStatus())
                .priceShowStatus(addReq.getPriceShowStatus())
                .storeShowStatus(addReq.getStoreShowStatus())
                .state(addReq.getState())
                .createId(addReq.getCreateId())
                .createName(addReq.getOperator())
                .build();

        // 新增时填充创建时间
        if (addReq.getId() == null) {
            goodsZone.setCreateTime(LocalDateTime.now());
            goodsZone.setGoodsNum(0); // 初始商品数为0
        }
        goodsZone.setUpdateTime(LocalDateTime.now());

        return goodsZone;
    }

    /**
     * 领域实体 → 响应VO
     * @param goodsZone 领域实体
     * @return 响应VO
     */
    public static GoodsZoneRes convertToVO(GoodsZone goodsZone) {
        if (goodsZone == null) {
            return null;
        }

        GoodsZoneRes res = new GoodsZoneRes();
        res.setId(goodsZone.getId());
        res.setGroupName(goodsZone.getGroupName());
        res.setGroupDesc(goodsZone.getGroupDesc());
        res.setBackgroundImg(goodsZone.getBackgroundImg());
        res.setSortType(goodsZone.getSortType());
        res.setSearchBoxStatus(goodsZone.getSearchBoxStatus());
        res.setPriceShowStatus(goodsZone.getPriceShowStatus());
        res.setStoreShowStatus(goodsZone.getStoreShowStatus());
        res.setGoodsNum(goodsZone.getGoodsNum());
        res.setState(goodsZone.getState());
        res.setCreateName(goodsZone.getCreateName());
        res.setCreateTime(goodsZone.getCreateTime());
        res.setUpdateTime(goodsZone.getUpdateTime());

        return res;
    }
}