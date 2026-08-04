package com.newzkl.platform.base.biz.market.model.vo.relation;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 商品关系列表查询
 * @date 2023/12/6 15:02
 */
@Data
public class GoodsRelationListVO {

    /** 主键ID */
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
     * 销售价 (Money, 落库 BIGINT 分)
     */
    private Money salePrice;


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
     * 市场价 (Money, 落库 BIGINT 分)
     */
    private Money marketPrice;
    /**
     * 最大利润
     */
    private Integer maxProfit;

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
     * 零售价 (Money, 落库 BIGINT 分)
     */
    private Money unitPrice;
    /**
     * 商品信息
     */
    private String goodsInfo;

    /** 是否选中 */
    private Boolean choose= false;
}
