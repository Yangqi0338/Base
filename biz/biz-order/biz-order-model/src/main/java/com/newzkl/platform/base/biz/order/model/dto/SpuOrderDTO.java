package com.newzkl.platform.base.biz.order.model.dto;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.biz.order.model.req.MemberOrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.support.api.SkuSaleInfo;
import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;
import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderExt;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/111:03
 */
@Data
public class SpuOrderDTO extends BaseDTO {

    /**
     * 订单类型 : 0 渠道商选品下单, 1 c端铺货下单
     */
    private OrderEnum.OrderType orderType;
    /**
     * 外部订单号
     */
    private String outOrderNo;
    private Long orderId;
    /**
     * 渠道类型 0 供货商品 1 自营商品
     */
    private SpuEnum.ChannelType spuChannelType;
    /**
     * 商品业务类型 (0:实物 1:虚拟 2:课程 3:服务)
     */
    private Integer spuSaleType;
    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 商户ID
     */
    private Long merchantId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 交易师ID
     */
    private Long dealerId;
    /**
     * 运营商ID
     */
    private Long operatorId;
    /**
     * SPU_ID
     */
    private Long spuId;
    /**
     * SPU名称
     */
    private String spuName;
    private String spuImg;
    /**
     * SKU总数
     */
    private Integer skuCount;
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
     * 运费金额
     */
    private Integer freightAmount;
    /**
     * 优惠金额
     */
    private Integer discountAmount;

    /**
     * C端支付金额
     */
    private Integer memberAmount;
    /**
     * 服务费: 渠道商应付
     */
    private Integer serviceAmount;
    /**
     * 订单状态 (0, "新订单"),(1,"C端待付款"),(2,"渠道商待付款"),(3,"运营商待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭"),
     */
    private OrderEnum.State orderState;
    /**
     * 收货信息值对象
     */
    private ShipVO shipVO;
    /**
     * 收货人手机号
     */
    private String shipPhone;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 订单状态流转日志,逗号隔开
     */
    private String orderStateLog;
    /**
     * 运费结算发送状态
     */
    private CommonEnum.YesOrNo settleSendState;
    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 账号id(account.id)
     */
    private Long accountId;

    private String storeName;

    private String storeHead;

    /**
     * 订单拓展信息
     */
    private SpuOrderExt spuOrderExt;

    /**
     * 账号id(account.id)
     */
    private Long memberId;
    /**
     * 发货完成时间
     */
    private LocalDateTime deliveredTime;
    /**
     * 确认收货时间
     */
    private LocalDateTime receiveTime;
    /**
     * 关闭时间
     */
    private LocalDateTime closeTime;

    private LocalDateTime createTime;
    /**
     * 售后中数量
     */
    private Integer refundingCount;

    private Integer refund;

    /**
     * 收益三方账号
     */
    private String benefitTripartiteId;

    /**
     * 初始化
     * @param orderCreateCommand
     * @param memberOrderCreateCommand
     * @param orderId
     * @param spuOrderId
     * @param orderAmountVO
     * @param spuVO
     * @param channelVO
     * @param freightAmount
     */
    public void init(OrderCreateCommand orderCreateCommand, MemberOrderCreateCommand memberOrderCreateCommand, Long orderId, Long spuOrderId, List<SkuOrderDTO> orderAmountVO, SkuSaleInfo spuVO, EarningsConfigRpcVO channelVO, Integer freightAmount) {
        this.id = spuOrderId;
        this.orderType = orderCreateCommand.getOrderType();
        this.outOrderNo = orderCreateCommand.getOutOrderNo();
        this.spuSaleType = spuVO.getSpuSaleType().getCode();
        this.spuChannelType = spuVO.getSpuChannelType();
        this.orderId = orderId;
        this.shipVO = orderCreateCommand.getShipVO();
        this.spuId = spuVO.getSpuId();
        this.storeId = memberOrderCreateCommand == null ? null: memberOrderCreateCommand.getStoreId();
        this.merchantId = memberOrderCreateCommand == null ? null: memberOrderCreateCommand.getMerchantId();
        this.accountId = memberOrderCreateCommand == null ? null: memberOrderCreateCommand.getAccountId();

        //订单金额
        this.goodsAmount = 0;
        this.freightAmount = freightAmount;
        this.discountAmount = 0;
        this.supplierAmount = 0;
        this.storeAmount = 0;
        this.serviceAmount = 0;
        this.setShipPhone(shipVO.getShipPhone());
        for (SkuOrderDTO skuOrder : orderAmountVO) {
            //只加属于该SPU订单的SKU
            if(skuOrder.getSpuId().equals(spuVO.getSpuId())){
                if(SpuEnum.ChannelType.CUSTOM == this.getSpuChannelType()){
                    this.storeAmount = this.storeAmount + skuOrder.getStoreAmount();
                }else if(SpuEnum.ChannelType.SELECTION == this.getSpuChannelType()){
                    this.supplierAmount = this.supplierAmount + skuOrder.getSupplierAmount();
                    this.goodsAmount = this.goodsAmount + skuOrder.getGoodsAmount();
                    this.storeAmount = this.storeAmount + skuOrder.getStoreAmount();
                    this.serviceAmount = this.serviceAmount + skuOrder.getTotalServiceChange();
                }else {
                    ThrowsException.exception(BaseErrorCode.PARAM);
                }
            }
        }
        this.orderState = OrderEnum.State.NEW;
        this.channelId = channelVO.getId();
        this.dealerId = channelVO.getUpDealerId();
        this.operatorId = channelVO.getUpOperatorId();
        this.supplierId = spuVO.getSupplierId();
        this.spuName = spuVO.getSpuName();
        this.spuImg = spuVO.getSpuImg();
        this.remark = StrUtil.isNotBlank(orderCreateCommand.getRemark())?orderCreateCommand.getRemark():"";
        this.orderStateLog = OrderEnum.State.NEW.getCode().toString();
        this.settleSendState = CommonEnum.YesOrNo.NO;
    }
}
