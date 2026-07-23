package com.newzkl.platform.base.biz.market.model.vo.market;

import lombok.Data;

/**
 * 铺货分类
 */
@Data
public class DistributionCategoryVO {

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;
}
