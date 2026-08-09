package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;


import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import org.dromara.autotable.annotation.Index;

/**
* 结算记录明细表
* @author fang
*/
@Data
@TableName
public class SettleRecordItemDO extends BaseDO {
	/**
	 * 结算记录ID
	 */
    @Index
	private Long settleRecordId;
	/**
	 * SPU_ID
	 */
    @Index
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