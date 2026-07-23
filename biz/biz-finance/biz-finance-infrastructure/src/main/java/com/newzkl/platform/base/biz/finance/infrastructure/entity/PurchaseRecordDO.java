package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.BaseUserDO;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.enums.order.OrderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
// TODO[pom-gap mybatis-plus-ext]: import org.dromara.mpe.autofill.annotation.JsonSerializable; (annotation 依赖延迟补)

/**
 * 购买记录 #pay
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class PurchaseRecordDO extends BaseUserDO {

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
    private Integer payAmount;

    /**
     * 商品金额
     */
    private Integer goodsAmount;

    /**
     * 支付方式
     */
    private OrderEnum.PayType payType;

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
    private String orderInfo;
}