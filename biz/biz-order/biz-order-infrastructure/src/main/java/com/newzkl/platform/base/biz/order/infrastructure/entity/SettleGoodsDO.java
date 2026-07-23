package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
* 结算商品信息表
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class SettleGoodsDO extends BaseDO {
	/**
	 * 供应商ID
	 */
    @Index
	private Long supplierId;
	/**
	 * SPU_ID
	 */
    @Index
	private Long spuId;
	/**
	 * 下次结算时间
	 */
    @Index
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
}