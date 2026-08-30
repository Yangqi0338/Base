package com.newzkl.platform.base.biz.order.model.dto;

import com.newzkl.platform.base.common.core.model.money.Money;


import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.vo.OrderSnapVO;
import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* 交易单
* @author fang
*/
@Data
public class OrderDTO extends BaseDTO {
	/**
	 * 订单类型 : 0 渠道商选品下单, 1 c端铺货下单
	 */
	private OrderEnum.OrderType orderType;
	/**
	 * 运营商ID
	 */
	private Long operatorId;
	/**
	 * 渠道商ID
	 */
	private Long channelId;
	/**
	 * 外部订单号
	 */
	private String outOrderNo;
	/**
	 * 外部平台来源(三方单) HUI_DING_HUO/LE_TAI 非外部单为 null
	 */
	private ThirdPartyOrderEnum.PlatformTypeEnum platformType;
	/**
	 * 收货信息值对象
	 */
	private ShipVO shipVO;
	/**
	 * 订单备注
	 */
	private String remark;
	/**
	 * 货款金额
	 */
	private Money supplierAmount;
	/**
	 * 选品金额
	 */
	private Money goodsAmount;
	/**
	 * 铺货金额
	 */
	private Money storeAmount;
	/**
	 * 服务费: 渠道商应付
	 */
	private Money serviceAmount;
	/**
	 * 选品运费
	 */
	private Money freightAmount;
	/**
	 * 自营运费
	 */
	private Money customFreightAmount;
	/**
	 * 优惠金额
	 */
	private Money discountAmount;
	/**
	 * 渠道商待支付金额
	 */
	private Money totalAmount;
	/**
	 * C端待支付金额
	 */
	private Money memberAmount;
	/**
	 * 订单状态  (0, "新订单"),(1,"C端待付款"),(2,"渠道商待付款"),(3,"运营商待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭"),
	 */
	private OrderEnum.State orderState;
	/**
	 * 支付时间
	 */
	private LocalDateTime payTime;
	private PaymentEnum.PayType payType;
	/**
	 * 订单状态流转日志,逗号隔开
	 */
	private String orderStateLog;
	/**
	 * 订单其他快照信息
	 */
	private OrderSnapVO orderSnapVO;
	/**
	 * 门店ID
	 */
	private Long storeId;
    /* 用户id */
    private Long memberId;

    /**
     * 账号id(account.id)
     */
    private Long accountId;

    private String userName;

    private String nickname;
	/**
	 * 支付流水
	 */
	private String payFlow;

    private LocalDateTime createTime;
    /**
     * 收益三方账号
     */
    private String benefitTripartiteId;


	/**
	 * 初始化
     * @param orderCreateCommand
     * @param orderId
     * @param spuOrderAmount
     */
	public void init(OrderCreateCommand orderCreateCommand, Long orderId, List<SpuOrderDTO> spuOrderAmount) {
		this.id = orderId;
		this.outOrderNo = orderCreateCommand.getOutOrderNo();
		this.shipVO = orderCreateCommand.getShipVO();
		this.operatorId = orderCreateCommand.getOperatorId();
		this.channelId = orderCreateCommand.getChannelId();
		this.remark = orderCreateCommand.getRemark();
		this.orderType = orderCreateCommand.getOrderType();
		//订单金额 (Money 累加; discountAmount 仍为 Integer 分, 参与 Money 运算时转 Money)
		this.goodsAmount = Money.ZERO;
		this.supplierAmount = Money.ZERO;
		this.storeAmount = Money.ZERO;
		this.freightAmount = Money.ZERO;
		this.customFreightAmount = Money.ZERO;
		this.discountAmount = Money.ZERO;
		this.serviceAmount = Money.ZERO;
		for (SpuOrderDTO spuOrder : spuOrderAmount) {
			this.discountAmount = this.discountAmount.add(spuOrder.getDiscountAmount());
            this.benefitTripartiteId = String.format("%s,%s", this.benefitTripartiteId, spuOrder.getBenefitTripartiteId());
			if(SpuEnum.ChannelType.SELECTION == spuOrder.getSpuChannelType() ||
					SpuEnum.ChannelType.OUT == spuOrder.getSpuChannelType()){
				this.supplierAmount = this.supplierAmount.add(spuOrder.getSupplierAmount());
				this.goodsAmount = this.goodsAmount.add(spuOrder.getGoodsAmount());
				this.storeAmount = this.storeAmount.add(spuOrder.getStoreAmount());
				this.freightAmount = this.freightAmount.add(spuOrder.getFreightAmount());
				this.serviceAmount = this.serviceAmount.add(spuOrder.getServiceAmount());
			}else {
				ThrowsException.exception(BaseErrorCode.PARAM);
			}
		}
		// 渠道商待付 = 选品 + 运费 + 服务费 - 优惠; C端待付 = 铺货 + 运费 - 优惠
		this.totalAmount = this.goodsAmount.add(this.freightAmount).add(this.serviceAmount).subtract(this.discountAmount);
		this.memberAmount = this.storeAmount.add(this.freightAmount).subtract(this.discountAmount);
		this.orderState = OrderEnum.State.NEW;
	}
}