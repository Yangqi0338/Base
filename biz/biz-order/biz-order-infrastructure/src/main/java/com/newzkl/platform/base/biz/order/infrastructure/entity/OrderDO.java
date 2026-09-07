package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.biz.order.model.vo.OrderExt;
import com.newzkl.platform.base.biz.order.model.vo.OrderSnapVO;
import com.newzkl.platform.base.common.ddd.model.vo.ShipVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.TableIndex;
import org.dromara.autotable.annotation.TableIndexes;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;

/**
 * 交易单数据对象
 *
 * <p>一次支付行为的顶层单据, 下挂 spu_order / sku_order 两级明细</p>
 *
 * @author muc_fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
@TableIndexes({
        @TableIndex(name = "key", type = IndexTypeEnum.UNIQUE, fields = {"orderNo", "delFlag"})
})
public class OrderDO extends BaseDO {

    /**
     * 交易单号
     */
    private String orderNo;

    /**
     * 订单类型
     */
    private OrderEnum.OrderType orderType;

    /**
     * 外部订单号
     */
    private String outOrderNo;

    /**
     * 外部平台来源
     * @ext 三方单来源, 非外部单为 null
     */
    private ThirdPartyOrderEnum.PlatformTypeEnum platformType;

    /**
     * 渠道商ID
     */
    @Index
    private Long channelId;

    /**
     * spuId
     * @ext String 冗余, 为多 spu 下单兼容 (当前仅单 spu)
     */
    private String spuId;

    /**
     * 收货信息
     */
    @JsonSerializable
    private ShipVO shipVO;

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
     * 订单状态
     */
    @Index
    private OrderEnum.State orderState;

    /**
     * C端支付状态
     */
    private CommonEnum.YesOrNo memberPayState;

    /**
     * 渠道商支付状态
     */
    private CommonEnum.YesOrNo channelPayState;

    /**
     * 支付时间
     */
    @Index
    private LocalDateTime payTime;

    /**
     * 支付方式
     */
    private PaymentEnum.PayType payType;

    /**
     * 订单状态流转日志
     * @ext 逗号分隔
     */
    private String orderStateLog;

    /**
     * 订单快照
     */
    @JsonSerializable
    private OrderSnapVO orderSnapVO;

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
     * 关闭时间
     */
    private LocalDateTime closeTime;

    /**
     * 是否有售后
     */
    private CommonEnum.YesOrNo refund;

    /**
     * 订单拓展信息(承接原 spu_order.spu_order_ext, 含取消/关闭原因与门店会员快照)
     */
    @JsonSerializable
    private OrderExt orderExt;
}
