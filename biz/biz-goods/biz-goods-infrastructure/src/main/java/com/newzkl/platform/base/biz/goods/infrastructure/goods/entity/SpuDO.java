package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

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
    @ColumnType(value = MysqlTypeConstant.TEXT)
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
    @ColumnType(value = MysqlTypeConstant.TEXT)
	private String detail;
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
	 * 品牌id
	 */
    @Index
	private Long brandId;
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
    private AccountEnum.Identity identity;
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
     * 扩展字段
     * @ext JSON, 存 categoryName/brandName/accountName/marketPriceBegan/marketPriceEnd/salePriceBegan/salePriceEnd/supplierPriceBegan/supplierPriceEnd
     */
    @JsonSerializable
    private String expand;
}
