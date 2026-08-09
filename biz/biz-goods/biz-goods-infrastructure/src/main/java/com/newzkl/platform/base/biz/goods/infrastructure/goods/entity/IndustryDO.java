package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
* 行业
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class IndustryDO extends BaseDO {
	/**
	 * 用户ID
	 */
    @Index
	private Long accountId;
	/**
	 * 名称
	 * @ext 查询
	 */
	private String name;
	/**
	 * 描述
	 */
    @TableField("`desc`")
	private String desc;
	/**
	 * 分类ID集合
	 */
    @Index
	private String categoryIdList;
}