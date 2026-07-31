package com.newzkl.platform.base.biz.order.model.dto;


import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.vo.OrderSnapVO;
import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* 交易单
* @author fang
*/
@Data
public class Order{
	/**
	 * 交易单ID
	 */
	private Long id;
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
	private Integer supplierAmount;
	/**
	 * 选品金额
	 */
	private Integer goodsAmount;
	/**
	 * 铺货金额
	 */
	private Integer storeAmount;
	/**
	 * 服务费: 渠道商应付
	 */
	private Integer serviceAmount;
	/**
	 * 选品运费
	 */
	private Integer freightAmount;
	/**
	 * 自营运费
	 */
	private Integer customFreightAmount;
	/**
	 * 优惠金额
	 */
	private Integer discountAmount;
	/**
	 * 渠道商待支付金额
	 */
	private Integer totalAmount;
	/**
	 * C端待支付金额
	 */
	private Integer memberAmount;
	/**
	 * 订单状态  (0, "新订单"),(1,"C端待付款"),(2,"渠道商待付款"),(3,"运营商待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭"),
	 */
	private OrderEnum.State orderState;
	/**
	 * 支付时间
	 */
	private LocalDateTime payTime;
	private Integer payType;
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
	public void init(OrderCreateCommand orderCreateCommand, Long orderId, List<SpuOrder> spuOrderAmount) {
		this.id = orderId;
		this.outOrderNo = orderCreateCommand.getOutOrderNo();
		this.shipVO = orderCreateCommand.getShipVO();
		this.operatorId = orderCreateCommand.getOperatorId();
		this.channelId = orderCreateCommand.getChannelId();
		this.remark = orderCreateCommand.getRemark();
		this.orderType = orderCreateCommand.getOrderType();
		//订单金额
		this.goodsAmount = 0;
		this.supplierAmount = 0;
		this.storeAmount = 0;
		this.freightAmount = 0;
		this.customFreightAmount = 0;
		this.discountAmount = 0;
		this.serviceAmount = 0;
		for (SpuOrder spuOrder : spuOrderAmount) {
			this.discountAmount = this.discountAmount + spuOrder.getDiscountAmount();
            this.benefitTripartiteId = String.format("%s,%s", this.benefitTripartiteId, spuOrder.getBenefitTripartiteId());
			if(SpuEnum.ChannelType.CUSTOM == spuOrder.getSpuChannelType()){
				this.storeAmount = this.storeAmount + spuOrder.getStoreAmount();
				this.customFreightAmount = this.customFreightAmount + spuOrder.getFreightAmount();
			}else if(SpuEnum.ChannelType.SELECTION == spuOrder.getSpuChannelType() ||
					SpuEnum.ChannelType.OUT == spuOrder.getSpuChannelType()){
				this.supplierAmount = this.supplierAmount + spuOrder.getSupplierAmount();
				this.goodsAmount = this.goodsAmount + spuOrder.getGoodsAmount();
				this.storeAmount = this.storeAmount + spuOrder.getStoreAmount();
				this.freightAmount = this.freightAmount + spuOrder.getFreightAmount();
				this.serviceAmount = this.serviceAmount + spuOrder.getServiceAmount();
			}else {
				ThrowsException.exception(BaseErrorCode.PARAM);
			}
		}
		this.totalAmount = this.goodsAmount + this.freightAmount + this.serviceAmount - this.discountAmount;
		this.memberAmount = this.storeAmount + this.freightAmount - this.discountAmount;
		this.orderState = OrderEnum.State.NEW;
	}
}