package com.newzkl.platform.base.biz.order.model.dto;

import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.biz.order.model.req.RefundCommand;
import com.newzkl.platform.base.biz.order.model.req.RefundItemCommand;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* 售后单
* @author fang
*/
@Data
public class RefundDTO extends BaseDTO {
	/**
	 * 外部售后单号
	 */
	private String outRefundId;
	/**
	 * 订单ID
	 */
	private Long orderId;
	/**
	 * SPU订单ID
	 */
	private Long spuOrderId;
	/**
	 * 申请人角色
	 */
	private Long createRole;
	/**
	 * 订单类型 : 0 渠道商选品下单, 1 c端铺货下单
	 */
	private OrderEnum.OrderType orderType;
	/**
	 * 渠道类型 0 供货商品 1 自营商品
	 */
	private SpuEnum.ChannelType spuChannelType;
	/**
	 * 门店ID
	 */
	private Long storeId;
	/**
	 * 客户ID
	 */
	private Long memberId;
	/**
	 * 渠道商ID
	 */
	private Long channelId;
	/**
	 * 供应商ID
	 */
	private Long supplierId;
	/**
	 * (0,"待渠道商审核"),(2,"待供应商审核"),(4,"待提交物流"),(6,"待确认收货"),(7,"待平台介入"),(8,"平台介入中"),(9,"退款中"),(10,"已完成"),(-2,"已拒绝"),(-4,"已关闭"),
	 */
	private RefundEnum.State refundState;
	/**
	 * 售后类型 0仅退款 1退货退款
	 */
	private RefundEnum.RefundType refundType;
	/**
	 * 售后运费金额
	 */
	private Money freightAmount;
	/**
     * 售后总金额
	 */
	private Money refundAmount;
    /**
     * 渠道商服务费
     */
    private Money serviceAmount;
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
	 * 售后原因
	 */
	private String reason;
	/**
	 * 申请说明
	 */
	private String remark;
	/**
	 * 申请图片
	 */
	private String images;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 物流公司名称
	 */
	private String freightCompanyName;
	/**
	 * 物流单号
	 */
	private String freightNo;
	/**
	 * 收货状态
	 */
	private Integer takeDeliveryState;
	/**
	 * 退款状态
	 */
	private Integer payState;
	/**
	 * 审核完成时间
	 */
	private LocalDateTime auditTime;
	/**
	 * 售后完成时间
	 */
	private LocalDateTime refundTime;
	/**
	 * 状态变化时间
	 */
	private LocalDateTime stateTime;

    /**
     * 商家自动确认截止时间
     */
    private LocalDateTime storeAutoTime;
	/**
	 * 来源状态
	 */
	private Integer fromState;
	/**
	 * 来源订单状态
	 */
	private OrderEnum.State fromOrderState;
	/**
	 * 审核日志
	 */
	private String auditLog;
	/**
	 * 售后流转状态
	 */
	private String refundStateLog;
	/**
	 * 售后明细
	 */
	private List<RefundItemVO> item;
	/**
	 * 拒绝原因
	 */
	private String refuseReason;

    private FreightExt freightExt;

	public void init(RefundCommand refundCommand, SpuOrderAggVO spuOrderAggVO, Map<Long, SkuRefundDTO> skuRefundResMap, Money freightAmount) {
		TransferUtils.transfer(refundCommand, this);
		this.setId(SnowflakeGenerator.getSnowflakeId());
		SpuOrderVO orderVO = spuOrderAggVO.getSpuOrderVO();
		Map<Long, SkuOrderVO> orderItemVOMap = spuOrderAggVO.getSkuOrderList().stream().collect(Collectors.toMap(SkuOrderVO::getSkuId, Function.identity()));
		Money refundAmount = Money.ZERO;
		Money supplierAmount = Money.ZERO;
		Money goodsAmount = Money.ZERO;
		Money storeAmount = Money.ZERO;
		List<RefundItemVO> refundItemList = new ArrayList<>();
		for (RefundItemCommand refundItemCommand : refundCommand.getRefundItemCommandList()) {
			SkuRefundDTO skuRefundRes = skuRefundResMap.get(refundItemCommand.getSkuId());
			SkuOrderVO orderItemVO = orderItemVOMap.get(refundItemCommand.getSkuId());
			RefundItemVO refundItem = new RefundItemVO();
			refundItem.setSpuId(skuRefundRes.getSpuId());
			refundItem.setSkuId(refundItemCommand.getSkuId());
			if(refundItemCommand.getCount() == null || refundItemCommand.getCount() == 0){
				refundItem.setCount(orderItemVO.getCount());
			}else {
				refundItem.setCount(refundItemCommand.getCount());
			}
			refundItem.setOutSkuId(orderItemVO.getOutSkuId());
			refundItem.setSpuName(orderVO.getSpuName());
			refundItem.setSpuImg(orderVO.getSpuImg());
			refundItem.setSkuSaleAttribute(orderItemVO.getSkuSaleAttribute());
            // 售后金额 = 铺货金额 + 运费 - 优惠
            refundItem.setRefundAmount(orderItemVO.getStoreAmount().add(orderItemVO.getFreightAmount()).subtract(orderItemVO.getDiscountAmount()));
            refundItem.setSkuStorePrice(orderItemVO.getSkuStorePrice());
			refundItem.setSupplierAmount(orderItemVO.getSupplierAmount());
			refundItem.setSkuOrderId(skuRefundRes.getSkuOrderId());
			refundItem.setRefundedCount(skuRefundRes.getRefundedCount());
			refundItem.setOrderCount(skuRefundRes.getOrderCount());
			refundAmount = refundAmount.add(refundItem.getRefundAmount());
			supplierAmount = supplierAmount.add(orderItemVO.getSupplierAmount());
			goodsAmount = goodsAmount.add(orderItemVO.getGoodsAmount());
			storeAmount = storeAmount.add(orderItemVO.getStoreAmount());
			refundItemList.add(refundItem);
		}
		this.orderType = orderVO.getOrderType();
		this.spuChannelType = orderVO.getSpuChannelType();
		this.storeId = orderVO.getStoreId();
		this.memberId = orderVO.getMemberId();
		this.setOrderId(orderVO.getOrderId());
		this.setSpuOrderId(orderVO.getId());
		this.setSupplierId(orderVO.getSupplierId());
		this.setChannelId(orderVO.getChannelId());
		this.setSupplierAmount(supplierAmount);
		this.setGoodsAmount(goodsAmount);
		this.setStoreAmount(storeAmount);
        // 售后总额 = 各明细售后金额 + 运费 (freightAmount 为 Integer 分入参)
        this.setRefundAmount(refundAmount.add(freightAmount));
        this.setServiceAmount(orderVO.getServiceAmount());
		this.setFreightAmount(freightAmount);
		this.setRefundState(RefundEnum.State.CHANNEL_WAIT);
		this.setRefundStateLog(RefundEnum.State.CHANNEL_WAIT.getCode().toString());
		this.setItem(refundItemList);
	}
}