package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;

/**
* spu
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class SpuDO extends BaseDO {
	/**
	 * 名称
	 * @ext 查询
	 */
    @Index
	private String name;
	/**
	 * 标题
	 * @ext 查询
	 */
	private String title;
	/**
	 * 轮播图
	 */
    @Size(max = 1000)
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
	 * 详情
	 */
	private String detail;
	/**
	 * 商品类型
	 */
    private SpuEnum.SaleType goodsType;
	/**
	 * 账号ID
	 */
    @Index
	private Long accountId;
	/**
	 * 所属平台分类
	 */
    @Index
	private Long categoryId;
	/**
	 * 所属平台分类名称完整
	 */
	private String categoryName;
	/**
	 * 品牌id
	 */
    @Index
	private Long brandId;
	/**
	 * 品牌名称
	 */
	private String brandName;
	/**
	 * 运费模板id
	 */
    @Index
	private Long freightTemplateId;
	/**
	 * 发货时效类型
	 */
    private SpuEnum.DeliverTimeType deliverTimeType;
	/**
	 * 最大发货天数
	 */
	private Integer maxDeliverDay;
	/**
     * 状态
	 */
    @Index
    private SpuEnum.State state;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 供应商销售金额
	 */
	private Money supplierSaleAmount;
	/**
	 * 账号名称
	 */
	private String accountName;
	/**
	 * 市场价
	 */
	private Money marketPrice;
	/**
	 * 供货价
	 */
	private Money supplyPrice;
	/**
     * 销售价
     */
    private Money salePrice;
    /**
     * 销售价
     */
    private Money unitPrice;
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
	private Money adminSaleAmount;
	/**
	 * 渠道商销售额
	 */
	private Money channelSaleAmount;
	/**
	 * 渠道类型
	 */
    private SpuEnum.ChannelType channelType;
	/**
	 * 外部供应链商品ID
	 */
	private String outSpuId;
	/**
	 * 角色ID
	 */
    @OldColumnName("role_id")
    private RoleEnum.CompanyRole role;
	/**
	 * 最大利润
	 */
	private Money maxProfit;
	/**
	 * 市场价起始
	 */
	private Money marketPriceBegan;
	/**
	 * 市场价结束
	 */
	private Money marketPriceEnd;
	/**
	 * 销售价起始
	 */
	private Money salePriceBegan;
	/**
	 * 销售价结束
	 */
	private Money salePriceEnd;
	/**
	 * 成交数量
	 */
	private Integer dealNum;
	/**
	 * 售后数量
	 */
	private Integer refundNum;
	/**
	 * 供货价起始
	 */
	private Money supplierPriceBegan;
	/**
	 * 供货价结束
	 */
	private Money supplierPriceEnd;
	/**
	 * 审批状态
	 */
    private AuditEnum.State auditState;
	/**
	 * 最后拒绝原因
	 * @ext 状态变更至待用户提交前的最后一次拒绝原因
	 */
	private String lastRefuseReason;
	/**
	 * 审批流ID
	 */
	private Long flowId;
	/**
	 * 外部商品状态
	 * @ext 0 已上架，1 已下架
	 */
	private Integer outState;

	/**
	 * 销售区域
	 */
	private String limitArea;
    /**
     * 规格类型
     */
    private SpuEnum.SpecType specType;
    /**
     * 最小计价数
     */
    private Double minPricingNum;
    /**
     * 供应商ID
     */
    @Index
    private Long supplierId;
}
