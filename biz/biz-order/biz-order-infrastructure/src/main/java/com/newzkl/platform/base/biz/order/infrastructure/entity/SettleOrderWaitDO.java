package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.dto.Money;

import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

import java.time.LocalDateTime;

/**
* 待结算订单信息表
* @author fang
*/
@Data
public class SettleOrderWaitDO extends BaseDO {
	/**
	 * 供应商ID
	 */
	private Long supplierId;
	/**
	 * SPU_订单ID
	 */
	private Long spuOrderId;
	/**
	 * sku订单ID
	 */
	private Long skuOrderId;
	/**
	 * 类型 0商品 1运费 2售后
	 */
	private Integer type;
	/**
	 * 订单结算金额
	 */
	private Money orderMoney;
	/**
	 * SPU_ID
	 */
	private Long spuId;
	/**
	 * SKU_ID
	 */
	private Long skuId;
	/**
	 * sku数量
	 */
	private Integer skuCount;
	/**
	 * 结算状态
	 */
	private Integer settleState;
	/**
	 * 结算时间
	 */
	private LocalDateTime settleTime;
	/**
	 * 售后单ID
	 */
	private Long refundId;
	/**
	 * 结算单ID
	 */
	private Long settleRecordId;
	/**
	 * 售后金额 默认0
	 */
	private Money refundAmount;

	/**
	 * 结算时间节点(时间戳)
	 */
	private Long settleTimeNode;
}