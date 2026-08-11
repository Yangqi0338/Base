package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderExt;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
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
@TableName(autoResultMap = true)
public class SpuOrderDO extends BaseDO {
    /**
     * 订单类型
     */
    private OrderEnum.OrderType orderType;
    /**
     * 渠道类型
     */
    private SpuEnum.ChannelType spuChannelType;
    /**
     * 外部订单号
     */
    @Index
    private String outOrderNo;
    /**
     * 交易单ID
     */
    @Index
    private Long orderId;
    /**
     * 渠道商ID
     */
    @Index
    private Long channelId;
    /**
     * 供应商ID
     */
    @Index
    private Long supplierId;
    /**
     * 交易师ID
     */
    @Index
    private Long dealerId;
    /**
     * 运营商ID
     */
    @Index
    private Long operatorId;
    /**
     * SPU_ID
     */
    @Index
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
     * SKU种类数量
     */
    private Integer skuCount;
    /**
     * 选品金额
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
     * 渠道商待支付总金额
     */
    private Money totalAmount;
    /**
     * 货款金额
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
    /**
     * 订单状态
     */
    @Index
    private OrderEnum.State orderState;

    /**
     * 收货信息
     */
    @JsonSerializable
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
     * 订单状态流转日志
     * @ext 逗号隔开
     */
    private String orderStateLog;
    /**
     * 运费结算发送状态
     */
    private CommonEnum.YesOrNo settleSendState;
    /**
     * 门店ID
     */
    @Index
    private Long storeId;
    /**
     * C端会员ID
     */
    @Index
    private Long memberId;
    /**
     * 账号id(account.id)
     */
    @Index
    private Long accountId;
    /**
     * 门店名称
     * @ext 下单快照
     */
    private String storeName;
    /**
     * 门店头像
     * @ext 下单快照
     */
    private String storeHead;
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

    /**
     * 是否有售后
     */
    private CommonEnum.YesOrNo refund;
}
