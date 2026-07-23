package com.newzkl.platform.base.biz.order.model.order.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

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