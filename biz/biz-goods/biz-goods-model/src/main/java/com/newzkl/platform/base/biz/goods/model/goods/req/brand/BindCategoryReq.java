package com.newzkl.platform.base.biz.goods.model.goods.req.brand;

import lombok.Data;

import java.io.Serializable;

/**
 * 行业-分类绑定请求
 */
@Data
public class BindCategoryReq implements Serializable {

    /** 行业ID */
    private Long industryId;

    /** 分类ID */
    private Long categoryId;
    /**
     * 是否绑定
     * @ext true 绑定, false 解绑
     */
    private Boolean isBind;
}