package com.newzkl.platform.base.biz.goods.model.goods.vo.spu;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.goods.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * spu
 *
 * @author fang
 */
@Data
public class SpuVO extends BaseRes implements Serializable {
    /**
     * 名称 (查询)
     */
    @NotEmpty(message = "name?")
    public String name;

    /**
     * 成交数量
     */
    private Integer dealNum;
    /**
     * ID (查询)
     */
    private Long id;
    /**
     * 编码 (查询)
     */
    private String code;
    /**
     * 标题 (查询)
     */
    private String title;
    /**
     * 轮播图
     */
    private String scrollImg;
    /**
     * ** 搜索关键字,逗号隔开
     */
    private String searchKey;
    /**
     * 图片
     */
    @NotEmpty(message = "img?")
    private String img;
    /**
     * 视频
     */
    private String video;
    /**
     * 详情
     */
    private String detail;
    /**
     * 渠道类型 0 供货商品 1 自营商品 2 外部商品
     */
    private SpuEnum.ChannelType channelType;
    /**
     * 外部供应链商品ID
     */
    private String outSpuId;
    /**
     * 商品类型 0:实物商品 1:课程 2:服务 (查询)
     */
    private Integer goodsType;
    /**
     * 账号ID (查询) 0 平台 1 怡亚通
     */
    private Long accountId;
    /**
     * 所属平台分类 (查询)
     */
    @NotNull(message = "categoryId?")
    private Long categoryId;
    /**
     * 所属平台分类名称完整
     */
    private String categoryName;
    /**
     * 品牌id (查询)
     */
    private Long brandId;
    /**
     * 品牌名称
     */
    private String brandName;
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
     * 状态 0:仓库中 2:上架中 3:待上架 (查询)
     */
     private Integer state;
    /**
     * 外部商品状态：0、已上架 1、已下架
     */
    private Integer outState;
    /**
     * 总销量
     */
    private Integer saleNum;
    /**
     * 虚拟销量
     */
    private Integer virtualSaleNum;
    /**
     * 选品数量
     */
    private Integer selectionNum;
    /**
     * 平台销售额
     */
    private Integer adminSaleAmount;
    /**
     * 渠道商销售额
     */
    private Integer channelSaleAmount;
    /**
     * 销售金额
     */
    private Integer saleAmount;
    /**
     * 售后数量
     */
    private Integer refundNum;
    /**
     * sku列表
     */
    @NotEmpty(message = "skuList?")
    private List<SkuVO> skuList;
    /**
     * 销售属性
     */
    @NotEmpty(message = "spuSaleAttributeList?")
    private List<SpuAttributeVO> spuSaleAttributeList;
    /**
     * 参数属性
     */
    private List<SpuAttributeVO> spuParamAttributeList;
    /**
     * 角色ID
     */
    private RoleEnum.CompanyRole role;
    /**
     * 供应商销售金额
     */
    private Integer supplierSaleAmount;
    /**
     * 最大利润
     */
    private Integer maxProfit;
    /**
     * 市场价起始
     */
    private Integer marketPriceBegan;
    /**
     * 市场价结束
     */
    private Integer marketPriceEnd;
    /**
     * 冗余: 账号名称
     */
    private String accountName;
    /**
     * 冗余: 市场价
     */
    private Integer marketPrice;
    /**
     * 冗余: 供货价
     */
    private Integer supplyPrice;
    /**
     * 冗余: 供货价起始
     */
    private Integer supplyPriceBegan;
    /**
     * 冗余: 供货价结束
     */
    private Integer supplyPriceEnd;
    /**
     * 冗余: 销售价
     */
    private Integer salePrice;
    /**
     * 冗余: 建议零售价
     */
    private Integer unitPrice;
    /**
     * 冗余: 毛利率
     */
    private Integer profit;
    /**
     * 冗余:审批ID
     */
    private Long flowId;
    /**
     * 冗余:审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过",4,"终止")
     */
    private Integer auditState;
    /**
     * 冗余:最后拒绝原因: 状态变更未待用户提交前的最后一次拒绝原因
     */
    private String lastRefuseReason;
    /**
     * 冗余: 销售价起始
     */
    private Integer salePriceBegan;
    /**
     * 冗余: 销售价结束
     */
    private Integer salePriceEnd;
    /**
     * 冗余: 供货价起始
     */
    private Integer supplierPriceBegan;
    /**
     * 冗余: 供货价结束
     */
    private Integer supplierPriceEnd;
    /**
     * 冗余: 是否是包邮模板
     */
    private Integer freePost;
    /**
     * 冗余: 是否限购
     */
    private Integer limitBuy;
    /**
     * 冗余: 是否上传了视频
     */
    private Integer uploadVideoFlag;

    /**
     * 让利比例
     */
    private Integer discountRate;
    /**
     * 规格类型
     * @see SpuEnum.SpecType
     */
    private String specType;
    /**
     * 最小计价数
     */
    private Double minPricingNum;

    /** 是否选中 */
    private Boolean choose = false;
    /**
     * 商品信息
     */
    private MarketGoodsInfoVO goodsInfo;
    /**
     * 市场数量
     */
    private Integer marketNum;

    /**
     * 行业名称
     */
    private String industryName;

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

    public void doDesensitized() {
        setAccountName(PatternUtil.desensitized(getAccountName(), 3, 2));
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