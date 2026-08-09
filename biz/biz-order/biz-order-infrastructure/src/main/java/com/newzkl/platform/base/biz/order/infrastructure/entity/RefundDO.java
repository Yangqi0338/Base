package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.money.Money;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.order.model.vo.FreightExt;
import com.newzkl.platform.base.biz.order.model.vo.RefundItemVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 退款
 * @date 2023/5/417:37
 */
@Data
@TableName(autoResultMap = true)
public class RefundDO extends BaseDO {

    /**
     * 交易单ID
     */
    private Long orderId;
    /**
     * 外部售后单号
     */
    private String outRefundId;
    /**
     * 申请人角色
     */
    private RoleEnum.CompanyRole createRole;
    /**
     * 订单类型
     */
    private OrderEnum.OrderType orderType;
    /**
     * 渠道类型
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
     * SPU订单ID
     */
    private Long spuOrderId;
    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 售后状态
     */
    private RefundEnum.State refundState;
    /**
     * 售后类型
     */
    private RefundEnum.RefundType refundType;
    /**
     * 运费金额
     */
    private Money freightAmount;
    /**
     * 退款金额
     */
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
    /**
     * 售后原因
     */
    private String reason;
    /**
     * 备注
     */
    private String remark;
    /**
     * 凭证图片
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
     * @ext 源列 take_delivery_state, 取值 0/1
     */
    private Integer takeDeliveryState;
    /**
     * 退款支付状态
     * @ext 源列 pay_state, 取值 0/1
     */
    private Integer payState;
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
    /**
     * 退款完成时间
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
     * 售后流转状态
     */
    private String refundStateLog;
    /**
     * 来源订单状态
     */
    private OrderEnum.State fromOrderState;
    /**
     * 售后明细
     * @ext JSON 结构 List&lt;RefundItemVO&gt;
     */
    @JsonSerializable
    private List<RefundItemVO> item;
    /**
     * 拒绝原因
     */
    private String refuseReason;
    /**
     * 外部售后地址
     * @ext JSON 结构 ApiRefundFreightAddressVO
     */
    private String outRefundAddress;

    /**
     * 运费扩展信息
     */
    @JsonSerializable
    private FreightExt freightExt;
}
