package com.newzkl.platform.base.biz.order.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
* 结算商品信息表
* @author fang
*/
@Data
public class SettleGoods{
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 供应商ID
	 */
	private Long supplierId;
	/**
	 * SPU_ID
	 */
	private Long spuId;
	/**
	 * 下次结算时间
	 */
	private LocalDateTime nextSettleTime;
	/**
	 * 结算次数
	 */
	private Integer settleNum;
	/**
	 * 结算金额
	 */
	private Integer settleMoney;
	/**
	 * 结算商品数量
	 */
	private Integer settleGoodsNum;
	/**
	 * 账期修改次数
	 */
	private Integer upNum;

	public void init() {
		this.settleNum = 0;
		this.settleMoney = 0;
		this.settleGoodsNum = 0;
		this.upNum = 0;
	}
}