package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
/**
* 运费模板
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class FreightTemplateDO extends BaseDO {
	/**
	 * 名称 查询
	 */
    @Index
	private String name;
	/**
	 * 是否包邮 0：不包邮  1：包邮
	 */
	private Integer freePost;
	/**
	 * 计价方式 1:按件数 2:按重量 3:按体积
	 */
	private Integer pricingManner;
	/**
	 * 是否指定条件包邮 0:否 1:是
	 */
	private Integer isFreePostCondition;
	/**
	 * 是否默认模板
	 */
	private Integer isDefault;
	/**
	 * 包邮条件
	 */
	private String freePostCondition;
	/**
	 * 地区运费规则
	 */
	private String regionSpec;
	/**
	 * 账号ID 查询
	 */
    @Index
	private Long accountId;
}