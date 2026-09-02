package com.newzkl.platform.base.biz.goods.model.goods.dto.spu;

import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * spu属性
 *
 * <p>继承 {@code BaseDTO} 承接 id/creatorId/executor/createTime/updateTime,
 * 使 DO → DTO → VO 两跳转换不丢审计字段</p>
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpuAttributeDTO extends BaseDTO {
	/**
	 * spuId (查询)
	 */
	private Long spuId;
	/**
	 * 类型 0:销售属性 1:参数属性 (查询)
	 */
	private SpuEnum.SpuAttributeType type;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 手动添加规格或参数的值，参数单值
	 */
	private String value;
}