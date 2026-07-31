package com.newzkl.platform.base.biz.order.model.req;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
* 结算记录表
* @author fang
*/
@Data
public class SettleRecordEditReq {
	/**
	 * ID
	 */
	@NotNull
	private Long id;
	/**
	 * 标签
	 */
	private String label;
}