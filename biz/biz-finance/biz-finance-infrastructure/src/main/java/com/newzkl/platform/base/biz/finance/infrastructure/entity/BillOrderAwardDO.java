package com.newzkl.platform.base.biz.finance.infrastructure.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 订单奖励账单 DO
 *
 * @author kc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class BillOrderAwardDO extends BaseDO {

    /**
     * 角色
     */
    private RoleEnum.CompanyRole role;

    /**
     * 账号id
     */
    @Index
    private Long accountId;

    /**
     * 用戶名即手机号
     */
    @Index
    private String username;

    /**
     * 钱包类型
     */
    @Index
    private PurseEnum.PurseType purseType;

    /**
     * 账户类型
     */
    @Index
    private PurseEnum.FinanceUser accountType;

    /**
     * 金额
     */
    private Money amount;

    /**
     * 订单数
     */
    private Integer orderCount;

}
