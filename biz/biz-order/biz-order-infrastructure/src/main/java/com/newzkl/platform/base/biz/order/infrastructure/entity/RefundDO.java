package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.dto.Money;

import com.newzkl.platform.base.biz.order.model.vo.FreightExt;
import com.newzkl.platform.base.biz.order.model.vo.RefundItemVO;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 退款
 * @date 2023/5/417:37
 */
@Data
public class RefundDO extends BaseDO {

    private Long orderId;
    /**
     * 外部售后单号
     */
    private String outRefundId;
    /**
     * 申请人角色
     */
    private Long createRole;
    /**
     * 订单类型 : 0 渠道商选品下单, 1 c端铺货下单
     */
    private Integer orderType;
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
    private Long spuOrderId;
    /**
     * 渠道商ID channel_id
     */
    private Long channelId;
    /**
     * 供应商ID supplier_id
     */
    private Long supplierId;
    private RefundEnum.State refundState;
     private RefundEnum.RefundType refundType;
    private Money freightAmount;
    private Money refundAmount;
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
     * 服务费
     */
    private Money serviceAmount;
    private String reason;
    private String remark;
    private String images;
    private String phone;
    private String freightCompanyName;
    private String freightNo;
    private Integer takeDeliveryState;
    private Integer payState;
    private LocalDateTime auditTime;
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
     * 售后流转状态
     */
    private String refundStateLog;
    /**
     * 来源订单状态
     */
    private OrderEnum.State fromOrderState;
    /**
     * 售后明细 格式: List<RefundItemVO>
     */
    @JsonSerializable
    private List<RefundItemVO> item;
    /**
     * 拒绝原因
     */
    private String refuseReason;
    /**
     * 外部售后地址 ApiRefundFreightAddressVO.class
     */
    private String outRefundAddress;

    @JsonSerializable
    private FreightExt freightExt;
}
