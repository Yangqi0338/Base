package com.newzkl.platform.base.biz.goods.model.goods.req.brand;

import lombok.Data;

import java.io.Serializable;

@Data
public class BindBrandReq implements Serializable {

    private Long categoryId;

    private Long brandId;
    /**
     * 绑定 true
     * 解绑 false
     */
    private Boolean isBind;
}