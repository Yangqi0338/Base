package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.mpe.autofill.annotation.JsonSerializable;
// TODO[pom-gap mybatis-plus-ext]: import org.dromara.mpe.autofill.annotation.JsonSerializable; (annotation 依赖延迟补)

/**
 * 购买记录 #pay
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
public class PurchaseRecordDO extends BaseDO {

    /**
     * 购买单号
     */
    @Index
    private String purchaseNo;

    /**
     * 购买类型
     */
    private PurseEnum.PurchaseRecordType type;

    /**
     * 交易单号
     */
    @Index
    private Long tradeNo;

    /**
     * 订单号
     */
    @Index
    private Long orderNo;

    /**
     * 客户id
     */
    @Index
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 支付金额
     */
    private Money payAmount;

    /**
     * 商品金额
     */
    private Money goodsAmount;

    /**
     * 支付方式
     */
    private PaymentEnum.PayType payType;

    /**
     * 支付状态
     */
    @Index
    private OrderEnum.State payState;

    /**
     * 三方交易单号
     */
    private String tripartiteTradeNo;

    /**
     * 外键id
     */
    @Index
    private Long foreignId;

    /**
     * 订单信息
     */
    // TODO[pom-gap mybatis-plus-ext]: @JsonSerializable (autofill json, 依赖延迟补)
    @JsonSerializable
    private String orderInfo;
}