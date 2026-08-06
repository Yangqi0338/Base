package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
/**
* sku
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class SkuDO extends BaseDO {
	/**
	 * 图片
	 */
	private String img;
	/**
	 * 条形码
	 */
	private String barCode;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 重量(千克)
	 */
	private Double weight;
	/**
	 * 体积(m3)
	 */
	private Double volume;
	/**
	 * spuId (查询)
	 */
    @Index
	private Long spuId;
	/**
	 * 市场价 (Money, 落库 BIGINT 分)
	 */
	private Money marketPrice;
	/**
	 * 供货价 (Money, 落库 BIGINT 分)
	 */
	private Money supplyPrice;
	/**
     * 销售价(to channel) (Money, 落库 BIGINT 分)
     */
    private Money salePrice;
    /**
     * 销售价(to c) (Money, 落库 BIGINT 分)
     */
    private Money unitPrice;
	/**
	 * 商品销售属性，json格式
	 */
	private String saleAttribute;
	/**
	 * 外部SkuId
	 */
	private String outSkuId;
	/**
	 * 销售价加价比例
	 */
	private Float salePriceRate;
	/**
	 * 起购数量
	 */
	private Integer buyStartQty;
    /**
     * 追加: 扩展字段
     */
    private String expand;
}