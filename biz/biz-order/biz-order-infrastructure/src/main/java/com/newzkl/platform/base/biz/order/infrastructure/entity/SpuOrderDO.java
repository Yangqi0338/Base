package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderExt;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2023/11/1014:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "spu_order", autoResultMap = true)
public class SpuOrderDO extends BaseDO {
    /**
     * 订单类型 : 0 渠道商选品下单, 1 c端铺货下单
     */
    @Index
    private Integer orderType;
    /**
     * 渠道类型 0 供货商品 1 自营商品
     */
    private SpuEnum.ChannelType spuChannelType;
    /**
     * 外部订单号
     */
    @Index
    private String outOrderNo;
    @Index
    private Long orderId;
    /**
     * 渠道商ID channel_id
     */
    @Index
    private Long channelId;
    /**
     * 商户ID
     */
    @Index
    private Long merchantId;
    /**
     * 供应商ID supplier_id
     */
    @Index
    private Long supplierId;
    /**
     * 交易师ID dealer_id
     */
    @Index
    private Long dealerId;
    /**
     * 运营商ID operator_id
     */
    @Index
    private Long operatorId;
    /**
     * spu_id
     */
    @Index
    private Long spuId;
    /**
     * spu_name
     */
    private String spuName;
    /**
     * spu_img
     */
    private String spuImg;
    private Integer skuCount;
    private Money goodsAmount;
    /**
     * 铺货金额
     */
    private Money storeAmount;
    private Money freightAmount;
    private Money discountAmount;
    private Money totalAmount;
    /**
     * 货款金额 supplier_amount
     */
    private Money supplierAmount;
    /**
     * C端支付金额
     */
    private Money memberAmount;
    /**
     * 服务费: 渠道商应付
     */
    private Money serviceAmount;
    @Index
    private Integer orderState;

    @JsonSerialize
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
    @Index
    private Long storeId;
    @Index
    private Long memberId;
    /**
     * 账号id(account.id)
     */
    @Index
    private Long accountId;
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
     * 订单拓展信息
     */
    @JsonSerializable
    private SpuOrderExt spuOrderExt;

    private Integer refund;
}
