package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

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
	 * 名称
	 */
	private String name;
	/**
	 * spuId
	 * @ext 查询
	 */
    @Index
	private Long spuId;
	/**
	 * 市场价
	 */
	private Money marketPrice;
	/**
	 * 供货价
	 */
	private Money supplyPrice;
	/**
     * 销售价(to channel)
     */
    private Money salePrice;
    /**
     * 销售价(to c)
     */
    private Money unitPrice;
	/**
	 * 商品销售属性
	 * @ext json格式
	 */
    @JsonSerializable
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
    @JsonSerializable
    private String expand;
}