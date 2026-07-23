package com.newzkl.platform.base.biz.order.model.order.dto;


import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.order.model.support.api.DistributionDetailVO;
import com.newzkl.platform.base.biz.order.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.biz.order.model.order.req.RefundItemReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundReq;
import com.newzkl.platform.base.biz.order.model.order.res.SkuRefundRes;
import com.newzkl.platform.base.biz.order.model.order.vo.FreightExtVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundItemVO;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
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
public class Refund {
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 外部售后单号
	 */
	private String outRefundId;
	/**
	 * 订单ID
	 */
	private String orderNo;
	/**
	 * SPU订单ID
	 */
	private String spuOrderNo;
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
	 * 商户ID
	 */
	private Long merchantId;
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
	private Integer refundType;
	/**
	 * 售后运费金额
	 */
	private Integer freightAmount;
	/**
     * 售后总金额
	 */
	private Integer refundAmount;
    /**
     * 渠道商服务费
     */
    private Integer serviceAmount;
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
	private Integer fromOrderState;
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

    private FreightExtVO freightExt;

	public void init(RefundReq refundCommand, SpuOrder orderVO, List<SkuOrder> skuOrders, Map<Long, SkuRefundRes> skuRefundResMap, Integer freightAmount) {
        TransferUtils.transfer(refundCommand, this);
		this.setId(SnowflakeIdAble.getSnowflakeId());
		Map<Long, SkuOrder> orderItemVOMap = skuOrders.stream().collect(Collectors.toMap(SkuOrder::getSkuId, Function.identity()));
		Integer refundAmount = 0;
		Integer supplierAmount = 0;
		Integer goodsAmount = 0;
		Integer storeAmount = 0;
        String goodsSnapshot = orderVO.getGoodsSnapshot();
        DistributionDetailVO bean = JSONUtil.toBean(goodsSnapshot, DistributionDetailVO.class);
        List<RefundItemVO> refundItemList = new ArrayList<>();
		for (RefundItemReq refundItemCommand : refundCommand.getRefundItemCommandList()) {
			SkuRefundRes skuRefundRes = skuRefundResMap.get(refundItemCommand.getSkuId());
            SkuOrder orderItemVO = orderItemVOMap.get(refundItemCommand.getSkuId());
			RefundItemVO refundItem = new RefundItemVO();
			refundItem.setSpuId(skuRefundRes.getSpuId());
			refundItem.setSkuId(refundItemCommand.getSkuId());
			if(refundItemCommand.getCount() == null || refundItemCommand.getCount() == 0){
				refundItem.setCount(orderItemVO.getBuyNum());
			}else {
				refundItem.setCount(refundItemCommand.getCount());
			}
			refundItem.setOutSkuId(orderItemVO.getOutSkuId().toString());
			refundItem.setSpuName(bean.getSpuName());
			refundItem.setSpuImg(bean.getSpuImg());
			refundItem.setSkuSaleAttribute(orderItemVO.getSkuSnapshot());
            Long amount = orderItemVO.getChannelDistributionAmount() + orderItemVO.getFreightAmount() - orderItemVO.getDiscountAmount();
            refundItem.setRefundAmount(amount.intValue());
            refundItem.setSkuStorePrice(bean.getSellPrice().intValue());
			refundItem.setSupplierAmount(orderItemVO.getChannelPurchaseAmount().intValue());
			refundItem.setSkuOrderNo(skuRefundRes.getSkuOrderNo());
			refundItem.setRefundedCount(skuRefundRes.getRefundedCount());
			refundItem.setOrderCount(skuRefundRes.getOrderCount());
			refundAmount = refundAmount + refundItem.getRefundAmount();
			supplierAmount = supplierAmount + orderItemVO.getChannelPurchaseAmount().intValue();
			goodsAmount = goodsAmount + orderItemVO.getChannelDistributionAmount().intValue();
			Integer storeAmountItem = orderItemVO.getChannelDistributionAmount().intValue();
			storeAmount = storeAmount + storeAmountItem;
			refundItemList.add(refundItem);
		}
		this.orderType = orderVO.getOrderType();
		this.spuChannelType = bean.getChannelType();
		this.storeId = orderVO.getStoreId();
		this.memberId = orderVO.getUserId();
		this.setOrderNo(orderVO.getOrderNo());
		this.setMerchantId(orderVO.getChannelId());
		this.setSpuOrderNo(orderVO.getSpuOrderNo());
		this.setSupplierId(orderVO.getSupplierId());
		this.setChannelId(orderVO.getChannelId());
		this.setSupplierAmount(supplierAmount);
		this.setGoodsAmount(goodsAmount);
		this.setStoreAmount(storeAmount);
        this.setRefundAmount(refundAmount + freightAmount);
        this.setServiceAmount(orderVO.getTotalServiceFee().intValue());
		this.setFreightAmount(freightAmount);
        this.setRefundState(RefundEnum.State.CHANNEL_WAIT);
		this.setRefundStateLog(RefundEnum.State.CHANNEL_WAIT.getCode().toString());
		this.setItem(refundItemList);
	}
}