package com.newzkl.platform.base.biz.order.model.dto;

import com.newzkl.platform.base.common.core.model.money.Money;


import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.vo.OrderExt;
import com.newzkl.platform.base.biz.order.model.vo.OrderSnapVO;
import com.newzkl.platform.base.common.ddd.model.vo.ShipVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
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
	 * 交易单号
	 *
	 * <p>业务可读单号, BusinessType.ORDER 发号(前缀 O + 日期时间 + 序列), 子表关联键</p>
	 */
	private String orderNo;
	/**
	 * 外部订单号
	 */
	private String outOrderNo;
	/**
	 * 订单来源平台(轴A) openapi(乐态)入口下的单为 LE_TAI 平台内部单为 null
	 * <p>与商品级 {@code OrderSkuVO.platformType}(轴B 供货平台)不是同一语义 不参与三方下单派发</p>
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
     * spuId
     *
     * <p>SpuOrder 层折叠: 承接原 spu_order.spu_id, String 冗余为多 spu 下单兼容(当前仅单 spu)</p>
     */
    private String spuId;

    /**
     * 关闭时间
     *
     * <p>SpuOrder 层折叠: 承接原 spu_order.close_time</p>
     */
    private LocalDateTime closeTime;

    /**
     * 是否有售后
     *
     * <p>SpuOrder 层折叠: 承接原 spu_order.refund</p>
     */
    private CommonEnum.YesOrNo refund;

    /**
     * C端支付状态
     */
    private CommonEnum.YesOrNo memberPayState;

    /**
     * 渠道商支付状态
     */
    private CommonEnum.YesOrNo channelPayState;

    /**
     * 订单拓展信息
     *
     * <p>SpuOrder 层折叠: 承接原 spu_order.spu_order_ext, 含取消/关闭原因与门店会员快照</p>
     */
    private OrderExt orderExt;


	/**
	 * 初始化
     * @param orderCreateCommand 建单命令
     * @param orderId 交易单ID
     * @param skuOrderAmount sku 订单明细(SpuOrder 层折叠后金额汇总源改 sku 级)
     */
	public void init(OrderCreateCommand orderCreateCommand, Long orderId, List<SkuOrderDTO> skuOrderAmount) {
		this.id = orderId;
		this.outOrderNo = orderCreateCommand.getOutOrderNo();
		this.shipVO = orderCreateCommand.getShipVO();
		this.operatorId = orderCreateCommand.getOperatorId();
		this.channelId = orderCreateCommand.getChannelId();
		this.remark = orderCreateCommand.getRemark();
		this.orderType = orderCreateCommand.getOrderType();
		this.platformType = orderCreateCommand.getPlatformType();
		//订单金额 (Money 累加; discountAmount 仍为 Integer 分, 参与 Money 运算时转 Money)
		this.goodsAmount = Money.ZERO;
		this.supplierAmount = Money.ZERO;
		this.storeAmount = Money.ZERO;
		this.freightAmount = Money.ZERO;
		this.customFreightAmount = Money.ZERO;
		this.discountAmount = Money.ZERO;
		this.serviceAmount = Money.ZERO;
		// merge-1.0: SpuEnum.ChannelType 已删 CUSTOM(自营)枚举值(1.0 去自营, "全部改为供应商"),
		// 故删除原 CUSTOM 分支; customFreightAmount 字段保留但恒为 0。
		// 注: 1.0 曾在 spuOrder 级累加 benefitTripartiteId, 折叠到 sku 级未沿用; 已定案三方分润不汇总。
		for (SkuOrderDTO skuOrder : skuOrderAmount) {
			this.discountAmount = this.discountAmount.add(skuOrder.getDiscountAmount());
			if(SpuEnum.ChannelType.SELECTION == skuOrder.getSpuChannelType() ||
					SpuEnum.ChannelType.OUT == skuOrder.getSpuChannelType()){
				this.supplierAmount = this.supplierAmount.add(skuOrder.getSupplierAmount());
				this.goodsAmount = this.goodsAmount.add(skuOrder.getGoodsAmount());
				this.storeAmount = this.storeAmount.add(skuOrder.getStoreAmount());
				this.freightAmount = this.freightAmount.add(skuOrder.getFreightAmount());
				this.serviceAmount = this.serviceAmount.add(skuOrder.getTotalServiceChange());
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