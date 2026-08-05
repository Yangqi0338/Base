package com.newzkl.platform.base.common.ddd.facade;


import lombok.Data;

@Data
public class GoodsSimpleVO {

    /** 商品id */
    private Long id;

    /** 商品名称 */
    private String name;

    /** 商品图片 */
    private String img;

    /** 分类名称 */
    private String categoryName;

}
