package com.newzkl.platform.base.biz.market.model.res.distribution;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 铺货商品列表返回对象
 * @date 2024/4/2 16:33
 */
@Data
public class DistributionGoodsListRes {

    /**
     * id
     */
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
     * 标题
     */
    private String title;

    /**
     * 商品图片
     */
    private String goodsPicture;

    /**
     * 售价
     */
    private Integer sellPrice;

    /**
     * 市场价
     */
    private Integer marketPrice;

    /**
     * 销量
     */
    private Integer sellNum;

    /**
     * 虚拟销量
     */
    private Integer virtualSaleNum;

    /**
     * 商品状态 0:下架  1：上架  -1：平台下架
     */
    private Integer goodsState;

    /**
     * 自营为0
     */
    private Long marketId;

    /**
     * 时间
     */
    private LocalDateTime time;

    /**
     * 商品状态 2:上架中 3:待上架
     */
    private Integer state;

    /**
     * 商品信息
     */
    private String goodsInfo;

    /**
     * 零售价
     */
    private Integer unitPrice;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 发货时效类型 0 三日内 1 大于三日 (查询)
     */
    private Integer deliverTimeType;

    /**
     * 最大发货天数
     */
    private Integer maxDeliverDay;
    /**
     * 供货价
     */
    private Integer supplierPrice;

    /**
     * 是否需要更新：0不需要，1需要
     */
    private Integer needUpdate;

    /**
     * 上架时间
     */
    private LocalDateTime upTime;

    /**
     * SKU ID集合（逗号分隔）
     */
    private String skuIds;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 门店Id
     */
    private String storeId;

    /**
     * 成交金额
     */
    private Integer transactionAmount;

    /**
     * 最小利润
     */
    private Integer minProfit;
}
