package com.newzkl.platform.base.biz.finance.infrastructure.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.BaseDO;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.biz.finance.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.finance.model.enums.user.identity.RoleEnum;
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
    private RoleEnum.CompanyRole role;
    /**
     * 保证金类型
     */
    private PaymentEnum.PromisePayType promisePayType;
    /**
     * 金额
     */
    private Integer amount;
    /**
     * 支付方式
     */
    private OrderEnum.PayType payType;
    /**
     * 支付凭证
     */
    private String certificateUrl;
}