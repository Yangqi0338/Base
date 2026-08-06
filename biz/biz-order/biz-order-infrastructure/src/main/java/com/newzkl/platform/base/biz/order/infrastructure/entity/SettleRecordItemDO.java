package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.money.Money;


import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

/**
* 结算记录明细表
* @author fang
*/
@Data
public class SettleRecordItemDO extends BaseDO {
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
	 * 结算商品数量
	 */
	private Integer settleGoodsNum;
	/**
	 * 结算运费
	 */
	private Money spuFreight;
}