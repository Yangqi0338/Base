package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
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
    private PurseEnum.PurseType purseType;

    /**
     * 客户类型
     */
    @Index
    private PurseEnum.FinanceUser accountType;

    /**
     * 收益
     */
    private Integer earnings;

    /**
     * 总收益
     */
    private Integer totalEarnings;

    /**
     * 三方余额
     */
    private Integer tripartiteAmount;
}