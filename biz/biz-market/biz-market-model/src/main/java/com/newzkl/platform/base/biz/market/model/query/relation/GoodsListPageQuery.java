package com.newzkl.platform.base.biz.market.model.query.relation;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author niu
 * @description: 查询商品列表请求对象
 * @date 2023/12/6 15:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsListPageQuery extends PageQuery {

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

    /** 绑定类型 */
    private Integer bindType;
    /**
     * 商品上下架状态 : 2 上架 3 下架
     */
    private String spuState;
    /**
     * 时间排序 1 升序 2 降序
     */
    private Integer timeSort;
    /**
     * 价格排序 1 升序 2 降序
     */
    private Integer priceSort;
    /**
     * 利润排序 1 升序 2 降序
     */
    private Integer profitSort;
    /**
     * 销售价左
     */
    private Integer salePriceL;

    /**
     * 销售价右
     */
    private Integer salePriceR;
}
