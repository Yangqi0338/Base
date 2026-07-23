package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
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
	 * 市场价
	 */
	private Integer marketPrice;
	/**
	 * 供货价
	 */
	private Integer supplyPrice;
	/**
     * 销售价(to channel)
     */
    private Integer salePrice;
    /**
     * 销售价(to c)
     */
    private Integer unitPrice;
    /**
	 * 库存
	 */
	private Integer inventory;
	/**
	 * 库存预警个数
	 */
	private Integer inventoryWarning;
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