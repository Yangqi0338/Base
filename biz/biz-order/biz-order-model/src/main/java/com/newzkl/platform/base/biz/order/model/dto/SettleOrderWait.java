package com.newzkl.platform.base.biz.order.model.dto;

import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
* 待结算订单信息表
* @author fang
*/
@Data
public class SettleOrderWait{
	/**
	 * ID
	 */
	private Long id;
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
	 * 类型 0 商品 1 运费 2 售后冲正
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
	 * 售后状态
	 */
	private RefundEnum.State refundState;


	/**
	 * 结算时间节点(时间戳)
	 */
	private Long settleTimeNode;
	public void init() {
		this.settleState = CommonEnum.YesOrNo.NO.getCode();
		this.refundState = RefundEnum.State.CHANNEL_WAIT;
	}
}