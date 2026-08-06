package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
* 待结算订单信息表
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class SettleOrderWaitDO extends BaseDO {
	/**
	 * 供应商ID
	 */
	@Index
	private Long supplierId;
	/**
	 * SPU_订单ID
	 */
	@Index
	private Long spuOrderId;
	/**
	 * sku订单ID
	 */
	@Index
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
	@Index
	private Long spuId;
	/**
	 * SKU_ID
	 */
	@Index
	private Long skuId;
	/**
	 * sku数量
	 */
	private Integer skuCount;
	/**
	 * 结算状态
	 */
	@Index
	private Integer settleState;
	/**
	 * 结算时间
	 */
	private LocalDateTime settleTime;
	/**
	 * 售后单ID
	 */
	@Index
	private Long refundId;
	/**
	 * 售后状态 0 待审核 1 退款中 2 已完成(对齐源 refund_state 列语义, 迁移补映射)
	 */
	private Integer refundState;
	/**
	 * 结算单ID
	 */
	@Index
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