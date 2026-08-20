package com.newzkl.platform.base.biz.finance.infrastructure.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 保证金流水
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class PromiseFlowDO extends BaseDO {
    /**
     * 账号ID (查询)
     */
    @Index
    private Long accountId;
    /**
     * 角色ID
     */
    @Index
    private AccountEnum.Identity identity;
    /**
     * 保证金类型
     */
    private PaymentEnum.PromisePayType promisePayType;
    /**
     * 金额
     */
    private Money amount;
    /**
     * 支付方式
     */
    private PaymentEnum.PayType payType;
    /**
     * 支付凭证
     */
    private String certificateUrl;
}