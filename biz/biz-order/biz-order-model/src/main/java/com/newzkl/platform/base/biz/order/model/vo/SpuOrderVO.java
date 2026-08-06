package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * SPU订单
 * @author fang
 */
@Data
public class SpuOrderVO extends BaseRes {

     /**
     * 订单ID
     */
     private Long id;
     /**
      * 联表:支付方式
      */
     private Integer payType;
     /**
      * 联表:支付时间
      */
     private LocalDateTime payTime;
     /**
      * 联表:支付流水
      */
     private String payFlow;
     /**
      * 订单类型 : 0 渠道商选品下单, 1 c端铺货下单
      */
     private OrderEnum.OrderType orderType;
     /**
      * 外部订单号
      */
     private String outOrderNo;
     /**
      * 商品业务类型 (0:实物 1:虚拟 2:课程 3:服务)
      */
     private Integer spuSaleType;
     /**
      * 渠道类型 0 供货商品 1 自营商品
      */
     private SpuEnum.ChannelType spuChannelType;
     /**
      * 交易单ID
      */
     private Long orderId;
     /**
      * 门店ID
      */
     private Long storeId;
     /**
      * C端ID
      */
     private Long memberId;
     /**
      * 商户ID
      */
     private Long merchantId;
     /**
      * 供应商ID
      */
     private Long supplierId;
     /**
      * 渠道商ID
      */
     private Long channelId;
    /**
     * 渠道商名称
     */
    private String channelName;
     /**
     * SKU总数
     */
     private Integer skuCount;
     /**
      * 货款金额
      */
     private Money supplierAmount;
     /**
     * 商品金额
     */
     private Money goodsAmount;
     /**
      * 铺货金额
      */
     private Money storeAmount;
     /**
     * 运费金额
     */
     private Money freightAmount;
     /**
     * 优惠金额
     */
     private Money discountAmount;
     /**
      * 服务费: 渠道商应付
      */
     private Money serviceAmount;
     /**
     * 订单状态 (0, "新订单"),(1,"C端待付款"),(2,"渠道商待付款"),(3,"运营商待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭"),
     */
     private OrderEnum.State orderState;
     /**
      * 收货信息
      */
     private String shipVO;
     /**
      * 商品ID
      */
     private Long spuId;
     /**
      * SPU名称
      */
     private String spuName;
     /**
      * SPU图片
      */
     private String spuImg;
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
     /**
      * 售后中数量
      */
     private Integer refundingCount;
     /**
      * 收货人手机号
      */
     private String shipPhone;
     /**
     * 用户名
     */
    private String nickname;
    /**
     * 用户账号/手机号
     */
    private String username;
    /**
     * 账号id
     */
    private Long accountId;
    /**
     * 实际支付
     */
    private Money memberAmount;
    /**
     * 下单时间
     */
    private LocalDateTime orderTime;

    /**
     * 扩展字段
     */
    private SpuOrderExt spuOrderExt;

    private String storeName;

    private String storeHead;

    /**
     * 是否可以发起售后
     */
    private Boolean refund;

    /**
     * 售后状态 当产生售后时有效
     *  CHANNEL_WAIT(0,"待渠道商审核"),
     *         SUPPLIER_WAIT(2,"待供应商审核"),
     *         FREIGHT_WAIT(4,"待提交物流"),
     *         RECEIVE_WAIT(6,"待确认收货"),
     *         PLATFORM_WAIT(7,"待平台介入"),
     *         PLATFORM_ING(8,"平台介入中"),
     *         MONEY_ING(9,"退款中"),
     *         SUCCESS(10,"已完成"),
     *         REFUSE(-2,"已拒绝"),
     *         CLOSE(-4,"已关闭"),
     */
    private RefundEnum.State refundState;
    /**
     * 售后类型 0仅退款 1退货退款
     */
     private RefundEnum.RefundType refundType;

    /**
     * 支付倒计时 前端展示用
     */
    private Long remainTime;

    /**
     * 实际支付
     * 由数据反推, 不一定准, 待落库 TODO
     */
    public Money getPayAmount() {
        // 实际支付 = 铺货 - 优惠 + 运费
        return this.storeAmount.subtract(this.discountAmount).add(this.freightAmount);
    }

    /**
     * 渠道商需扣减金额
     * 由数据反推, 不一定准, 待落库 TODO
     */
    public Money getChannelDeductAmount() {
        return this.goodsAmount.add(this.freightAmount).add(this.serviceAmount);
    }

    public Boolean getRefund() {
        // 1. 先判断订单状态是否为12/99（空值则不满足该条件）
        boolean isOrderStateForbid = (orderState != null) && (orderState == OrderEnum.State.SUCCESS || orderState == OrderEnum.State.CLOSE);

        // 2. 再判断skuCount是否等于refundingCount（需先校验非空）
        boolean isSkuCountEqualRefunding = false;
        if (skuCount != null && refundingCount != null) {
            isSkuCountEqualRefunding = skuCount.equals(refundingCount);
        }

        // 3. 核心逻辑：满足任一禁止条件则返回false，否则返回true
        return !isOrderStateForbid && !isSkuCountEqualRefunding;
    }
}