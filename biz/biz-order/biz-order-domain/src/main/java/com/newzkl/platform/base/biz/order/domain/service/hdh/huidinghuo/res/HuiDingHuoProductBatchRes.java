package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 批量商品响应类（继承泛型基类，指定data类型为DataDTO）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoProductBatchRes extends HuiDingHuoBaseRes<HuiDingHuoProductBatchRes.DataDTO> {

    /**
     * 数据内容封装类（当前接口的data类型）
     */
    @Data
    public static class DataDTO {
        private Integer total; // 商品总数量
        private List<Item> itemList; // 商品数据列表
    }

    /**
     * 商品数据类
     */
    @Data
    public static class Item {
        private String id; // 商品id
        private Long brandId; // 品牌id
        private String brandName; // 品牌名称
        private String groupId; // 组商品id
        private String firstCateName; // 一级类目名称
        private String secondCateName; // 二级类目名称
        private String thirdCateName; // 三级类目名称
        private Long firstCateId; // 一级类目id
        private Long secondCateId; // 二级类目id
        private Long thirdCateId; // 三级类目id
        private String currency; // 货币单位
        private String name; // 商品名称
        private String originalName; // 商品原始标题
        private String shopName; // 店铺名称
        private String modifier; // 修饰词
        private String desc; // 商品描述
        private String richDesc; // 图文详情 html片段
        private List<String> sellingPoints; // 商品卖点
        private Integer gift; // 是否赠品( 0不是 1是)
        private Integer presell; // 是否预售商品(0不是 1是)
        private Integer shelfStatus; // 商品上下架状态(0下架 1上架)
        private Integer isDelete; // 是否删除(0否，1是)
        private List<String> imageList; // 图片数组
        private List<SkuNameValue> skuNameValues; // 商品总规格信息
        private List<Sku> skuList; // 商品规格信息
        private ItemLimitCondition itemLimitCondition; // 商品限购信息
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
        private List<String> imageUrlList; // 规格对应的图片数组
    }

    /**
     * 商品规格详情类
     */
    @Data
    public static class Sku {
        private String skuId; // 规格id
        private String itemId; // 商品id（与上层商品id一致）
        private BigDecimal weight; // 重量
        private BigDecimal weightG; // 重量(g)
        private String unit; // 单位
        private Integer unitQuantity; // 单位对应的数量
        private List<String> upcList; // 条形码数组
        private String upc; // 条形码（多个以/分割）
        private Integer totalStock; // 商品总数量
        private List<SkuDetailValue> skuValues; // 规格信息
        private List<SkuChannel> skuChannels; // 对应渠道信息
    }

    /**
     * 规格详情值类（用于skuList）
     */
    @Data
    public static class SkuDetailValue {
        private String skuName; // 规格名
        private String skuValue; // 规格值
        private String imageUrl; // 规格图
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
        private String actId; // 活动ID
        private BigDecimal actPrice; // 活动价
        private Date actEndTime; // 活动结束时间
        private Integer actTotalBuyNum; // 活动总购买数量限制
        private Integer stock; // 当前渠道商品的库存数
        private String itemCode; // 商品编码
        private Integer salesStatus; // 商品销售状态(0为不可售 1可售)
        private String expressTemplateId; // 运费模板id
        private Integer minBuyNum; // 单笔最小购买数量
        private Integer maxBuyNum; // 单笔最大购买数量
        private List<Material> materials; // 素材

        public BigDecimal getProfit() {
            return NumberUtil.sub(originalPrice, price);
        }
    }

    /**
     * 素材信息类
     */
    @Data
    public static class Material {
        private Integer type; // 素材类型，1：前置素材，2：后置素材
        private String imgUrl; // 素材Url
        private Date startTime; // 有效期开始时间
        private Date endTime; // 有效期结束时间
        private String marketDesc; // 营销文案
    }

    /**
     * 商品限购信息类
     */
    @Data
    public static class ItemLimitCondition {
        private Integer cycleLimitQuantity; // 周期限购数量
        private Integer cycleLimitTime; // 周期限购时间（默认单位：日）
        private Integer singleLimitSkuMinQuantity; // 单次限购规格最小数量
        private Integer singleLimitSkuMaxQuantity; // 单次限购规格最大数量
    }
}
