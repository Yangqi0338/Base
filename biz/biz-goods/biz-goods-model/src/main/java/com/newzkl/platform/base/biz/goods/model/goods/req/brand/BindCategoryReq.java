package com.newzkl.platform.base.biz.goods.model.goods.req.brand;

import lombok.Data;

import java.io.Serializable;

@Data
public class BindCategoryReq implements Serializable {

    private Long industryId;

    private Long categoryId;
    /**
     * 绑定 true
     * 解绑 false
     */
    private Boolean isBind;
}