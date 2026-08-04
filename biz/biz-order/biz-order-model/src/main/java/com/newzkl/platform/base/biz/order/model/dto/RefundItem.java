package com.newzkl.platform.base.biz.order.model.dto;

import com.newzkl.platform.base.common.core.model.dto.Money;

import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.Data;

/**
* 售后单明细
* @author fang
*/
@Data
public class RefundItem{
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 售后单ID
	 */
	private Long refundId;
	/**
	 * skuId
	 */
	private Long skuId;
	/**
	 * 退款数量
	 */
	private Integer count;
	/**
	 * 售后金额
	 */
	private Money refundAmount;

	public void init() {
		this.id = SnowflakeIdAble.getSnowflakeId();
	}
}