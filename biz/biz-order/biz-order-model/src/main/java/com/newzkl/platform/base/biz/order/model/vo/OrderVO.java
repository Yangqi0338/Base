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
 * 交易单(SpuOrder 层折叠后, 合并原 SpuOrderVO 字段, 语义 order 级)
 * @author fang
 */
@Data
public class OrderVO extends BaseRes {
     /**
     * 交易单ID
     */
     private Long id;
     /**
      * 交易单号
      */
     private String orderNo;
     /**
      * 订单类型 : 0 渠道商选品下单, 1 c端铺货下单
      */
     private OrderEnum.OrderType orderType;
     /**
      * 外部订单号
      */
     private String outOrderNo;
     /**
     * 收货信息值对象
     */
     private String shipVO;
     /**
     * 商品类型 (0:实物 1:课程 2:服务) 查询
     */
     private Integer goodsType;
     /**
      * 运营商ID
      */
     private Long operatorId;
     /**
     * 渠道商ID 查询
     */
     private Long channelId;
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
      * 服务费: 渠道商应付
      */
     private Money serviceAmount;
     /**
      * 渠道商待支付金额
      */
     private Money totalAmount;
     /**
      * C端待支付金额
      */
     private Money memberAmount;
     /**
     * 支付时间
     */
     private LocalDateTime payTime;
     private Integer payType;
     /**
      * 订单状态 (0, "新订单"),(1,"C端待付款"),(2,"渠道商待付款"),(3,"运营商待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭"),
      */
     private OrderEnum.State orderState;
     /**
      * 订单状态流转日志,逗号隔开
      */
     private String orderStateLog;
     /**
      * 支付流水
      */
     private String payFlow;

    /**
     * 购买方式
     */
    private String buyMode;

    /**
     * 用户名
     */
    private String nickname;

    // ===== SpuOrder 层折叠合并字段(语义 order 级) =====
    /**
     * 商品业务类型 (0:实物 1:虚拟 2:课程 3:服务)
     */
    private Integer spuSaleType;
    /**
     * 渠道类型 0 供货商品 1 自营商品
     */
    private SpuEnum.ChannelType spuChannelType;
    /**
     * 交易单ID(折叠后与 id 同值, 兼容原 SpuOrderVO.orderId 消费)
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
     * 渠道商名称
     */
    private String channelName;
    /**
     * SKU总数
     */
    private Integer skuCount;
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
     * 用户账号/手机号
     */
    private String username;
    /**
     * 账号id
     */
    private Long accountId;
    /**
     * 下单时间
     */
    private LocalDateTime orderTime;
    /**
     * 扩展字段
     */
    private OrderExt orderExt;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 门店头像
     */
    private String storeHead;
    /**
     * 是否可以发起售后
     */
    private Boolean refund;
    /**
     * 售后状态 当产生售后时有效
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
     *
     * <p>由数据反推, 不一定准, 待落库 TODO</p>
     * @return 实际支付金额 = 铺货 - 优惠 + 运费
     */
    public Money getPayAmount() {
        return this.storeAmount.subtract(this.discountAmount).add(this.freightAmount);
    }

    /**
     * 渠道商需扣减金额
     *
     * <p>由数据反推, 不一定准, 待落库 TODO</p>
     * @return 选品 + 运费 + 服务费
     */
    public Money getChannelDeductAmount() {
        return this.goodsAmount.add(this.freightAmount).add(this.serviceAmount);
    }

    /**
     * 是否可发起售后
     *
     * @return 订单状态非完成/关闭 且 售后中数量未覆盖全部 sku 时为 true
     */
    public Boolean getRefund() {
        boolean isOrderStateForbid = (orderState != null) && (orderState == OrderEnum.State.SUCCESS || orderState == OrderEnum.State.CLOSE);
        boolean isSkuCountEqualRefunding = false;
        if (skuCount != null && refundingCount != null) {
            isSkuCountEqualRefunding = skuCount.equals(refundingCount);
        }
        return !isOrderStateForbid && !isSkuCountEqualRefunding;
    }
}