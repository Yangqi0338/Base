package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品详情响应类（支持批量查询单个/多个商品）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoProductDetailRes extends HuiDingHuoBaseRes<List<HuiDingHuoProductDetailRes.Item>> {

    /**
     * 商品数据类（与批量查询的商品结构一致）
     */
    @Data
    public static class Item {
        private String id; // 商品id
        private Long brandId; // 品牌id
        private String brandName; // 品牌名称
        private String groupId; // 组商品id（可选）
        private String firstCateName; // 一级类目名称
        private String secondCateName; // 二级类目名称
        private String thirdCateName; // 三级类目名称
        private Long firstCateId; // 一级类目id
        private Long secondCateId; // 二级类目id
        private Long thirdCateId; // 三级类目id
        private String currency; // 货币单位
        private String name; // 商品名称
        private String originalName; // 商品原始标题（可选）
        private String shopName; // 店铺名称
        private String modifier; // 修饰词（可选）
        private String desc; // 商品描述（可选）
        private String richDesc; // 图文详情html（可选）
        private List<String> sellingPoints; // 商品卖点（可选）
        private Integer gift; // 是否赠品(0不是 1是)
        private Integer presell; // 是否预售(0不是 1是)
        private Integer shelfStatus; // 上下架状态(0下架 1上架)
        private Integer isDelete; // 是否删除(0否 1是)
        private List<String> imageList; // 图片数组
        private List<SkuNameValue> skuNameValues; // 商品总规格信息（可选）
        private List<Sku> skuList; // 商品规格信息（可选）
        private ItemLimitCondition itemLimitCondition; // 商品限购信息（可选）
    }

    /**
     * 商品总规格信息类
     */
    @Data
    public static class SkuNameValue {
        private String skuName; // 规格名称
        private List<SkuValue> skuValues; // 规格名对应的规格信息
    }

    /**
     * 规格值信息类（用于skuNameValues）
     */
    @Data
    public static class SkuValue {
        private String skuValue; // 规格值
        private List<String> imageUrlList; // 规格对应的图片数组（可选）
    }

    /**
     * 商品规格详情类
     */
    @Data
    public static class Sku {
        private String skuId; // 规格id
        private String itemId; // 商品id（与上层商品id一致）
        private BigDecimal weight; // 重量（可选）
        private BigDecimal weightG; // 重量(g)（可选）
        private String unit; // 单位（如：件、组）
        private Integer unitQuantity; // 单位对应的数量
        private List<String> upcList; // 条形码数组（可选）
        private String upc; // 条形码（多个以/分割，可选）
        private Integer totalStock; // 商品总数量
        private List<SkuDetailValue> skuValues; // 规格信息（可选）
        private List<SkuChannel> skuChannels; // 对应渠道信息
    }

    /**
     * 规格详情值类（用于skuList）
     */
    @Data
    public static class SkuDetailValue {
        private String skuName; // 规格名
        private String skuValue; // 规格值
        private String imageUrl; // 规格图（可选）
    }

    /**
     * 渠道信息类
     */
    @Data
    public static class SkuChannel {
        private String channelName; // 渠道名称
        private String channelType; // 渠道类型
        private BigDecimal price; // 现价（实际采购价）
        private BigDecimal originalPrice; // 原价（建议零售价）
        private String actId; // 活动ID（可选）
        private BigDecimal actPrice; // 活动价（可选）
        private Date actEndTime; // 活动结束时间（可选）
        private Integer actTotalBuyNum; // 活动总购买数量限制（可选）
        private Integer stock; // 当前渠道商品的库存数
        private String itemCode; // 商品编码
        private Integer salesStatus; // 商品销售状态(0不可售 1可售)
        private String expressTemplateId; // 运费模板id（可选）
        private Integer minBuyNum; // 单笔最小购买数量（可选）
        private Integer maxBuyNum; // 单笔最大购买数量（可选）
        private List<Material> materials; // 素材（可选）
    }

    /**
     * 素材信息类
     */
    @Data
    public static class Material {
        private Integer type; // 素材类型（1：前置素材，2：后置素材）
        private String imgUrl; // 素材Url（可选）
        private Date startTime; // 有效期开始时间（可选）
        private Date endTime; // 有效期结束时间（可选）
        private String marketDesc; // 营销文案（可选）
    }

    /**
     * 商品限购信息类
     */
    @Data
    public static class ItemLimitCondition {
        private Integer cycleLimitQuantity; // 周期限购数量（可选）
        private Integer cycleLimitTime; // 周期限购时间（默认单位：日，可选）
        private Integer singleLimitSkuMinQuantity; // 单次限购规格最小数量（可选）
        private Integer singleLimitSkuMaxQuantity; // 单次限购规格最大数量（可选）
    }
}