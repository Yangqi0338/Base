package com.newzkl.platform.base.biz.goods.rpc.model.openapi;

import com.newzkl.platform.base.biz.goods.model.enums.goods.SpuEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品spu信息
 *
 * @author wqm
 * @since 2023年04月27日 14:58:00
 */
@Data
public class ApiSpuVO implements Serializable {
    /**
     * SPU_ID
     */
    @NotNull
    private Long id;

    /**
     * 成交数量
     */
    private Integer dealNum;

    /**
     * 编码
     */
    private String code;
    /**
     * 商品名称
     */
    @NotNull
    private String name;
    /**
     * 标题
     */
    private String title;
    /**
     * 轮播图 : url字符串 逗号隔开
     */
    private String scrollImg;
    /**
     * 图片
     */
    @NotNull
    private String img;
    /**
     * 视频
     */
    private String video;
    /**
     * 详情 : 格式为 url字符串, 逗号隔开
     */
    private String detail;
    /**
     * 商品类型 0:实物商品, 大于0:虚拟, 1:课程, 2:服务
     */
    private Integer goodsType;
    /**
     * 所属平台分类
     */
    @NotNull
    private Long categoryId;
    /**
     * 所属平台分类名称
     */
    @NotNull
    private String categoryName;
    /**
     * 品牌id
     */
    @NotNull
    private Long brandId;
    /**
     * 品牌名称
     */
    @NotNull
    private String brandName;
    /**
     * 售卖状态 0 下架 1 上架
     */
    @NotNull
    private Integer saleState;
    /**
     * 是否包邮 0 不包邮 1 包邮
     */
    @NotNull
    private Integer freePost;

    /**
     * 冗余: 建议零售价(to c)
     */
    private Integer unitPrice;

    /**
     * 让利比例
     */
    private Integer discountRate;

    /**
     * 渠道类型 0 供货商品 1 自营商品 2 外部商品
     */
    private SpuEnum.ChannelType channelType;

    /**
     * 销售属性: 如: 颜色,尺码
     */
    @NotNull
    private List<ApiSpuAttributeVO> saleAttributeList;
    /**
     * 参数属性: 附加的 kv 描述数据
     */
    private List<ApiSpuAttributeVO> paramAttributeList;

    /**
     * 总销量
     */
    private Integer saleNum;
    /**
     * 虚拟销量
     */
    private Integer virtualSaleNum;

    /**
     * 状态
     */
    private Integer state;
    /**
     * 冗余: 供货价起始
     */
    private Integer supplierPriceBegan;
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

    /**
     * 商品信息
     */
    private MarketGoodsInfoVO goodsInfo;

    /**
     * 市场商品关系表中的商品信息
     */
    @Data
    public static class MarketGoodsInfoVO implements Serializable{

        /**
         * 赠送LT积分
         */
        private Integer giftLTPoints;

        /**
         * 商品标签
         */
        private String label;

        /**
         * 任务红包比例
         */
        private Integer taskRedPacketRatio;

    }

}
