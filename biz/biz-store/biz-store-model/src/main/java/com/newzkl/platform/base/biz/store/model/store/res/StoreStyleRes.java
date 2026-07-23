package com.newzkl.platform.base.biz.store.model.store.res;

import lombok.Data;

/**
 * 脉脉通商城页面门店样式VO
 */
@Data
public class StoreStyleRes {
    /**
     * 门店id
     */
    private Long id;

    /**
     * 样式名称
     */
    private String styleName;

    /**
     * 样板店id
     */
    private Long modelShopId;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 样板店名称
     */
    private String modelShopName;

    /**
     * 样板店描述
     */
    private String modelDescription;

    /**
     * 样式code
     */
    private String styleCode;

    /**
     * 样式内容
     */
    private String styleContent;

    /**
     * 商品id集合
     */
    private String goodsIdListStr;

    /**
     * 预览图
     */
    private String previewImage;

}
