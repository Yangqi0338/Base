package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;

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
	 * 交易单号
	 *
	 * <p>原 spu_order_id 列, slug42 起已存 order 主键值, 本次改名并转业务单号</p>
	 */
	@Index
	@OldColumnName("spu_order_id")
	private String orderNo;
	/**
	 * sku订单ID
	 */
	@Index
	private Long skuOrderId;
	/**
	 * 类型
	 * @ext 0 商品 1 运费 2 售后
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
	private CommonEnum.YesOrNo settleState;
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
	 * 售后状态
	 * @ext 对齐源 refund_state 列语义, 迁移补映射
	 */
	private RefundEnum.State refundState;
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
	 * 结算时间节点
	 * @ext 时间戳
	 */
	private Long settleTimeNode;
}