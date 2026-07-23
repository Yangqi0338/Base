package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
/**
* 操作日志
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class ExecuteLogDO extends BaseDO {
	/**
	 * 操作类型
	 */
	private Integer type;
	/**
	 * 操作主键
	 */
    @Index
	private Long targetId;
	/**
	 * 操作人ID
	 */
    @Index
	private Long executeUserId;
	/**
	 * 操作人名称
	 */
	private String executeUserName;
	/**
	 * 原数据
	 */
	private String oldData;
	/**
	 * 改动数据
	 */
	private String updateData;
}