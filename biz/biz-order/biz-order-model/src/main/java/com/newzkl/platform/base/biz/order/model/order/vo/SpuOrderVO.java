package com.newzkl.platform.base.biz.order.model.order.vo;

import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;
import com.newzkl.platform.base.biz.order.model.support.api.ChannelOutRes;
import com.newzkl.platform.base.biz.order.model.support.api.ChannelRes;
import com.newzkl.platform.base.biz.order.model.support.api.AccountGroupVO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * SPU级订单明细表 DTO
 * 纯业务属性，无数据库注解和技术细节
 *
 * @author sijiwang
 */
@Data
public class SpuOrderVO implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * spu单号
     */
    private String spuOrderNo;

    /**
     * 交易单号（关联交易订单主表）
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private Integer orderState;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 外部单号（第三方系统订单号）
     */
    private String outOrderNo;

    /**
     * 收益三方账号
     */
    private String benefitTripartiteId;

    /**
     * 是否外部订单：0-否 1-是
     */
    private Integer isExternalOrder;

    /**
     * 来源类型
     */
    private Integer sourceType;

    /**
     * 订单状态流转日志（逗号隔开）
     */
    private String orderStateLog;

    /**
     * 运费结算发送状态
     */
    private Integer settleFreightState;

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * 外部SPU ID
     */
    private Long outSpuId;

    /**
     * 商品数量
     */
    private Integer goodsQuantity;

    /**
     * 商品快照（JSON格式：SPU名称/图片/规格等）
     */
    private String goodsSnapshot;

    /**
     * 订单地址ID（关联地址表）
     */
    private Long shipAddressId;

    /**
     * 收货信息（JSON格式）
     */
    private String receiptInfo;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 平台进货总金额（单位：分）
     */
    private Long platformPurchaseAmount;

    /**
     * 平台铺货总金额（单位：分）
     */
    private Long platformDistributionAmount;

    /**
     * 渠道商进货总金额（单位：分）
     */
    private Long channelPurchaseAmount;

    /**
     * 渠道商铺货总金额（单位：分）
     */
    private Long channelDistributionAmount;

    /**
     * 门店销售总金额（单位：分）
     */
    private Long storeSalesAmount;

    /**
     * 订单应付总额（单位：分）
     */
    private Long orderPayableAmount;

    /**
     * 订单实付总额（单位：分）
     */
    private Long orderActualAmount;

    /**
     * 运费金额（单位：分）
     */
    private Long freightAmount;

    /**
     * 优惠金额（单位：分）
     */
    private Long discountAmount;

    /**
     * 平台服务费（单位：分）
     */
    private Long platformServiceFee;

    /**
     * 平台服务费率
     */
    private Double platformServiceRatio;

    /**
     * 运营商服务费（单位：分）
     */
    private Long operatorServiceFee;

    /**
     * 运营商服务费率
     */
    private Double operatorServiceRatio;

    /**
     * 总服务费（单位：分）
     */
    private Long totalServiceFee;

    /**
     * 总服务费率
     */
    private Double totalServiceRatio;

    /**
     * 用户支付金额（单位：分）
     */
    private Long userPayAmount;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 支付流水号
     */
    private String payFlowNo;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 确认收货时间
     */
    private LocalDateTime receiveTime;

    /**
     * 是否正在售后：0-否 1-是
     */
    private Integer isRefunding;

    /**
     * 售后数量
     */
    private Integer refundQuantity;

    /**
     * 订单关闭原因
     */
    private String closeReason;

    /**
     * 拓展字段（JSON格式）
     */
    private String extendInfo;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 子订单列表
     */
    private List<SkuOrderVO> skuOrderList;

    /**
     * 用户账号信息
     */
    private AccountGroupVO memberAccount;

    /**
     * 渠道账号商信息
     */
    private AccountGroupVO channelAccount;

    /**
     * 门店信息
     */
    private StoreRPCVO store;

    /**
     * 渠道商信息
     */
    private ChannelRes channel;

    /**
     * 是否可以发起售后
     */
    private Boolean refund;

    /**
     * 售后状态 当产生售后时有效
     *  CHANNEL_WAIT(0,"待渠道商审核"),
     *         MERCHANT_WAIT(1,"待商户审核"),
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
    private Integer refundState;
    /**
     * 售后类型 0仅退款 1退货退款
     */
    private Integer refundType;

    /**
     * 支付倒计时 前端展示用
     */
    private Long remainTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public Boolean getRefund() {
        // 1. 先判断订单状态是否为12/99（空值则不满足该条件）
        boolean isOrderStateForbid = (orderState != null) && (orderState == 12 || orderState == 99);

        // 2. 再判断skuCount是否等于refundingCount（需先校验非空）
        boolean isSkuCountEqualRefunding = false;
        if (goodsQuantity != null && refundQuantity != null) {
            isSkuCountEqualRefunding = goodsQuantity.equals(refundQuantity);
        }

        // 3. 核心逻辑：满足任一禁止条件则返回false，否则返回true
        return !isOrderStateForbid && !isSkuCountEqualRefunding;
    }
}