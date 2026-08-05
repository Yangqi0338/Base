package com.newzkl.platform.base.biz.goods.model.goods.dto.spu;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description: spu 操作对象
 * @date 2023/10/1713:33
 */
@Data
public class SpuDTO {
    /**
     * ID (查询)
     */
    private Long id;
    /**
     * 编码 (查询)
     */
    private String code;
    /**
     * 渠道类型 0 供货商品 1 自营商品
     * @see SpuEnum.ChannelType
     */
    private SpuEnum.ChannelType channelType;
    /**
     * 角色ID
     */
    private RoleEnum.CompanyRole role;
    /**
     * 商品类型 0:实物商品 1:课程 2:服务 (查询)
     */
    @NotNull(message = "goodsType?")
    private Integer goodsType;
    @NotNull(message = "name?")
    private String name;
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
     * 自定义类型. 0 供应商商品 (查询)
     */
    private Integer customType;
    /**
     * 账号ID (查询)
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
     * sku列表
     */
    @NotEmpty(message = "skuList?")
    private List<SkuDTO> skuList;
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
     * 外部供应链商品ID
     */
    private String outSpuId;
    /**
     * 外部商品状态：0、已上架 1、已下架
     */
    private Integer outState;
    /**
     * 销售区域
     */
    private String limitArea;
    /**
     * 内部属性: 审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过",4,"终止")
     */
    private Integer auditState;
    /**
     * 内部属性: 审批ID
     */
    private Long flowId;
    /**
     * 内部属性: 最后拒绝原因: 状态变更未待用户提交前的最后一次拒绝原因
     */
    private String lastRefuseReason;
    /**
     * 冗余: 销售价起始 (Money, 落库 BIGINT 分)
     */
    private Money salePriceBegan;
    /**
     * 冗余: 销售价结束 (Money, 落库 BIGINT 分)
     */
    private Money salePriceEnd;
    /**
     * 冗余: 供货价起始 (Money, 落库 BIGINT 分)
     */
    private Money supplierPriceBegan;
    /**
     * 冗余: 供货价结束 (Money, 落库 BIGINT 分)
     */
    private Money supplierPriceEnd;
    /**
     * 冗余: 账号名称
     */
    private String accountName;
    /**
     * 冗余: 市场价 (Money, 落库 BIGINT 分)
     */
    private Money marketPrice;
    /**
     * 冗余: 供货价 (Money, 落库 BIGINT 分)
     */
    private Money supplyPrice;
    /**
     * 冗余: 销售价(to channel) (Money, 落库 BIGINT 分)
     */
    private Money salePrice;
    /**
     * 冗余: 建议零售价(to c) (Money, 落库 BIGINT 分)
     */
    private Money unitPrice;
    /**
     * 规格类型
     * @see SpuEnum.SpecType
     */
    private String specType;
    /**
     * 最小计价数
     */
    private Double minPricingNum;
    /**
     * 刷新spu
     */
    private Boolean refresh;
    /**
     * 市场价起始 (Money, 落库 BIGINT 分)
     */
    private Money marketPriceBegan;
    /**
     * 市场价结束 (Money, 落库 BIGINT 分)
     */
    private Money marketPriceEnd;
    /**
     * 最大利润 (Money, 落库 BIGINT 分)
     */
    private Money maxProfit;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 供应商ID
     */
    private Long supplierId;
}
