package com.newzkl.platform.base.biz.market.model.vo.market;

import lombok.Data;

import java.util.List;

/**
 * @author niu
 * @description: 市场商品分类
 * @date 2024/5/20 10:57
 */
@Data
public class MarketGoodsCategoryVO {

    /** 主键ID */
    private Long id;

    /** 名称 */
    private String name;

    /** 图片 */
    private String img;

    /** 二级分类 */
    private List<MarketGoodsCategoryVO> twoCategory;
}
