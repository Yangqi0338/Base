package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * @author 客户账户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class AccountPurseDO extends BaseDO {

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
     * 账户类型
     */
    @Index
    private PurseEnum.Type purseType;

    /**
     * 客户类型
     */
    @Index
    private PurseEnum.User accountType;

    /**
     * 账户余额
     */
    private Money amount;

    /**
     * 累计入账总额
     */
    private Integer totalAmount;
}