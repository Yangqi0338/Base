package com.newzkl.platform.base.biz.market.model.res.distribution;


import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 铺货商品详情
 */
@Data
public class DistributionGoodsDetailRes implements Serializable {
    private static final long serialVersionUID = 1L;

    /******************************StoreGoods字段begin******************************************/
    private Long id;
    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 总销量
     */
    private Integer saleNum;
    /**
     * 虚拟销量
     */
    private Integer virtualSaleNum;

    /**
     * 成交数量
     */
    private Integer dealNum;

    /**
     * skuId
     */
    private Long skuId;

    /**
     * 轮播图 : url字符串 逗号隔开
     */
    private String scrollImg;

    /**
     * 图片
     */
    private String img;

    /**
     * 视频
     */
    private String video;
    /**
     * 来源
     * @see com.newzkl.platform.base.common.ddd.model.enums.goods.DistributionEnum.Source
     */
    private Long marketId;

    /**
     * 数据类型 0：商品  1：sku
     */
    private Integer dataType;

    /**
     * 销售价 (Money, 落库 BIGINT 分)
     */
    private Money sellPrice;

    /**
     * 销量
     */
    private Integer sellNum;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 商品状态 0:下架  1：上架  -1：回收站
     */
    private Integer goodsState;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 供货价 (Money, 落库 BIGINT 分)
     */
    private Money unitPrice;

    /**
     * 供货价 (Money, 落库 BIGINT 分)
     */
    private Money supplierPrice;

    /**
     * 商品信息
     */
    private String goodsInfo;

    /**
     * 是否需要更新：0不需要，1需要
     */
    private Integer needUpdate;

    /**
     * 上架时间
     */
    private LocalDateTime upTime;

    /******************************StoreGoods字段end******************************************/


    /******************************ApiSpuVO字段begin******************************************/
    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsPicture;

    /**
     * 商品类型 0:实物商品 1:课程 2:服务 (查询)
     */
    private Integer goodsType;

    /**
     * 冗余: 品牌名称
     */
    private String brandName;

    /**
     * 冗余: 所属平台分类名称完整
     */
    private String categoryName;

    /**
     * 销售属性: 如: 颜色,尺码
     */
    private List<Attribute> saleAttributeList;
    /**
     * 参数属性: 附加的 kv 描述数据
     */
    private List<Attribute> paramAttributeList;

    /**
     * 详情 : 格式为 url字符串, 逗号隔开
     */
    private String detail;
    /**
     * 运费模板id (查询)
     */
    private Long freightTemplateId;
    /**
     * 运费模板名称
     */
    private String freightTemplateName;
    /**
     * 发货时效类型 0 三日内 1 大于三日 (查询)
     */
    private Integer deliverTimeType;
    /**
     * 最大发货天数
     */
    private Integer maxDeliverDay;

    /******************************ApiSpuVO字段end******************************************/

    /**
     * 门店用户im账号
     */
    private String userAccount;

    /**
     * sku详情
     */
    private List<SkuDetail> skuDetailList;

    @Data
    public static class Attribute implements Serializable{
        /**
         * 名称
         */
        private String name;
        /**
         * 值
         */
        private String value;
    }


    @Data
    public static class SkuDetail {
        /**
         * 铺货ID
         */
        private Long distributionId;
        /**
         * SKU_ID
         */
        private Long id;
        /**
         * 图片
         */
        private String img;
        /**
         * 条形码
         */
        private String barCode;
        /**
         * 商品销售属性
         */
        private List<Attribute> saleAttribute;
        /**
         * 重量(千克)
         */
        private Double weight;
        /**
         * 体积(m3)
         */
        private Double volume;
        /**
         * spuId
         */
        private Long spuId;
        /**
         * 市场价 (Money, 落库 BIGINT 分)
         */
        private Money marketPrice;
        /**
         * 采购价 (Money, 落库 BIGINT 分)
         */
        private Money salePrice;
        /**
         * 起购数量
         */
        private Integer buyStartQty;

        /**
         * 供货价 (Money, 落库 BIGINT 分)
         */
        private Money supplyPrice;

        /**
         * 冗余: 建议零售价(to c) (Money, 落库 BIGINT 分)
         */
        private Money unitPrice;

    }

    /**
     * 商品视频
     */
    private List<VideoVO> videoList;

    @Data
    public static class VideoVO implements Serializable {
        /**
         * 视频路径
         */
        private String path;
        /**
         * 商品id
         */
        private Long spuId;

        /**
         * 封面路径
         */
        private String coverPath;
    }
}