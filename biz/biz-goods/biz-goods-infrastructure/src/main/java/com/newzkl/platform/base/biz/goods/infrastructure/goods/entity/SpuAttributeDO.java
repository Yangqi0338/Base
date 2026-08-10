package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

/**
* spu属性
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class SpuAttributeDO extends BaseDO {
	/**
	 * spuId
	 * @ext 查询
	 */
    @Index
	private Long spuId;
	/**
	 * 类型
	 * @ext 查询
	 */
    private SpuEnum.SpuAttributeType type;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 手动添加规格或参数的值，参数单值
	 */
    @ColumnType(value = MysqlTypeConstant.TEXT)
	private String value;
}