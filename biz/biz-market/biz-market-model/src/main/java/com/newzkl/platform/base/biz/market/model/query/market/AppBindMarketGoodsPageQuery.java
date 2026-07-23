package com.newzkl.platform.base.biz.market.model.query.market;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;



@Data
@EqualsAndHashCode(callSuper = true)
public class AppBindMarketGoodsPageQuery extends PageQuery {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 市场id
     */
    private Long marketId;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 商品ID集合
     */
    private List<Long> goodsIdList;
    /**
     * 关系类型：  1：一级市场商品  2：二级市场商品  3：市场选品商品
     */
    private Integer relationType;

    private Integer bindType;
    /**
     * 商品上下架状态 :  0:仓库中 2:上架中 3:待上架
     */
    private String spuState;

    private List<String> descs;
    private List<String> ascs;

}
