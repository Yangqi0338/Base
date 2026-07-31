package com.newzkl.platform.base.biz.goods.model.goods.req.brand;

import lombok.Data;

import java.io.Serializable;

@Data
public class BindBrandReq implements Serializable {

    /** 分类ID */
    private Long categoryId;

    /** 品牌ID */
    private Long brandId;
    /**
     * 绑定 true
     * 解绑 false
     */
    private Boolean isBind;
}