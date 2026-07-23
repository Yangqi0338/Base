package com.newzkl.platform.base.biz.store.model.template.res;

import lombok.Data;

/**
 * 样板店样式VO
 */
@Data
public class ModelShopStyleRes {

    private Long id;

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
     * 是否正在使用
     */
    private boolean inUse = false;

    /**
     * 预览图
     */
    private String previewImage;

}
