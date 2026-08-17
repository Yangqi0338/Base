package com.newzkl.platform.base.biz.store.model.store.entity;

import lombok.Data;

/**
* 门店
* @author fang
*/
@Data
public class Store{
    /**
     * 主键
     */
    private Long id;
    /**
     * 门店名称
     */
    private String name;
    /**
     *
     */
    private String logo;
    /**
     * 地址
     */
    private String address;
    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 商户ID
     */
    private Long merchantId;
    /**
     * 样板店ID
     */
    private Long modelShopId;
    /**
     * 是否是样板店
     */
    private Integer isModelShop;

    /**
     * 样式code
     */
    private String styleCode;

    /**
     * 门店类型
     */
    private Long type;

    /**
     * 门店类型名称
     */
    private String typeName;

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

    /**
     * 样式名称
     */
    private String styleName;

}