package com.newzkl.platform.base.biz.order.model.dto;

import com.newzkl.platform.base.common.core.model.money.Money;

import lombok.Data;

/**
* 结算记录明细表
* @author fang
*/
@Data
public class SettleRecordItemDTO {
	/**
	 * ID
	 */
	private Long id;

	/**
	 * 结算记录ID
	 */
	private Long settleRecordId;

	/**
	 * SPU_ID
	 */
	private Long spuId;
	/**
	 * spu名称
	 */
	private String spuName;
	/**
	 * spu图片
	 */
	private String spuImg;
	/**
	 * sku订单结算信息
	 */
	private String skuSettleDetail;
	/**
	 * 结算金额
	 */
	private Money settleMoney;

	/**
	 * 结算运费
	 */
	private Money spuFreight;
	/**
	 * 结算商品数量
	 */
	private Integer settleGoodsNum;

	public void init() {
	}
}