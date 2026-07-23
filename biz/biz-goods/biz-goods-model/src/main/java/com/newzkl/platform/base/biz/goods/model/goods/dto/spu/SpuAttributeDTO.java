package com.newzkl.platform.base.biz.goods.model.goods.dto.spu;

import lombok.Data;

/**
* spu属性
* @author fang
*/
@Data
public class SpuAttributeDTO {
	/**
	 * ID (查询)
	 */
	private Long id;
	/**
	 * spuId (查询)
	 */
	private Long spuId;
	/**
	 * 类型 0:销售属性 1:参数属性 (查询)
	 */
	private Integer type;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 手动添加规格或参数的值，参数单值
	 */
	private String value;
}