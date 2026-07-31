package com.newzkl.platform.base.biz.goods.model.goods.req.brand;

import lombok.Data;

import java.io.Serializable;

@Data
public class BindCategoryReq implements Serializable {

    /** 行业ID */
    private Long industryId;

    /** 分类ID */
    private Long categoryId;
    /**
     * 绑定 true
     * 解绑 false
     */
    private Boolean isBind;
}