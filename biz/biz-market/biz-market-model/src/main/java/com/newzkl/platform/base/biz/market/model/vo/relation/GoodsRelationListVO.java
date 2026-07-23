package com.newzkl.platform.base.biz.market.model.vo.relation;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 商品关系列表查询
 * @date 2023/12/6 15:02
 */
@Data
public class GoodsRelationListVO {

    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsPicture;

    /**
     * 售价区间
     */
    private String sellPrice;

    /**
     * 销售价
     */
    private Integer salePrice;


    /**
     * 销售价起始
     */
    private Integer salePriceBegan;

    /**
     * 销售价结束
     */
    private Integer salePriceEnd;

    /**
     * 销量
     */
    private Integer sellNum;

    /**
     * 客户销量
     */
    private Integer clientSellNum;

    /**
     * 时间
     */
    private LocalDateTime time;

    /**
     * 商品状态 2:上架中 3:待上架
     */
    private Integer state;

    /**
     * 选品量
     */
    private Integer selectionNum;

    /**
     * 市场id
     */
    private Long marketId;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 市场价
     */
    private Integer marketPrice;
    /**
     * 最大利润
     */
    private Integer maxProfit;
    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 让利比例
     */
    private Integer discountRate;

    /**
     * 冗余: 供货价起始
     */
    private int supplierPriceBegan;

    /**
     * 冗余: 供货价结束
     */
    private int supplierPriceEnd;

    /**
     * 零售价
     */
    private Integer unitPrice;
    /**
     * 商品信息
     */
    private String goodsInfo;

    private Boolean choose= false;
}
