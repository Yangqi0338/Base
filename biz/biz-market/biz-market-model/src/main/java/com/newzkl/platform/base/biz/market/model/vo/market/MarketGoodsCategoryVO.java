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

    private Long id;

    private String name;

    private String img;

    private List<MarketGoodsCategoryVO> twoCategory;
}
