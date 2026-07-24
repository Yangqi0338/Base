package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
* 结算记录表
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class SettleRecordDO extends BaseDO {
	/**
	 * 供应商ID
	 */
    @Index
	private Long supplierId;
	/**
	 * 结算时间(版本号)
	 */
    @Index
	private LocalDateTime settleTime;
	/**
	 * 结算金额
	 */
	private Integer settleMoney;
	/**
	 * 结算商品数量
	 */
	private Integer settleGoodsNum;
	/**
	 * 货款金额
	 */
	private Integer goodsAmount;
	/**
	 * 运费金额
	 */
	private Integer freightAmount;
	/**
	 * 售后金额
	 */
	private Integer refundAmount;
	/**
	 * 标签
	 */
	private String label;
}