package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import com.newzkl.platform.base.biz.goods.model.enums.goods.SpuEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
* spu属性
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class SpuAttributeDO extends BaseDO {
	/**
	 * spuId (查询)
	 */
    @Index
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