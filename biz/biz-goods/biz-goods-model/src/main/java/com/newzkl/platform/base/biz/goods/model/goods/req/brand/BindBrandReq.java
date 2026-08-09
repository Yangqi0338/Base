package com.newzkl.platform.base.biz.goods.model.goods.req.brand;

import lombok.Data;

import java.io.Serializable;

/**
 * 分类-品牌绑定请求
 */
@Data
public class BindBrandReq implements Serializable {

    /** 分类ID */
    private Long categoryId;

    /** 品牌ID */
    private Long brandId;
    /**
     * 是否绑定
     * @ext true 绑定, false 解绑
     */
    private Boolean isBind;
}