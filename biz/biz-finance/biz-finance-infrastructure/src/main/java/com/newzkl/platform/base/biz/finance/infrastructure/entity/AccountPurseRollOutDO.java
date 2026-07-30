package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.biz.finance.model.enums.AuditEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * @author 转出申请
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class AccountPurseRollOutDO extends BaseDO {

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
     * 客户类型
     */
    @Index
    private PurseEnum.FinanceUser accountType;

    /**
     * 账户类型
     */
    @Index
    private PurseEnum.PurseType purseType;

    /**
     * 申请金额
     */
    private Integer applyAmount;

    /**
     * 审核状态
     */
    private AuditEnum.State auditState;

    /**
     * 三方交易状态
     */
    private AuditEnum.Action tripartiteTradeState;

    /**
     * 三方用户id
     */
    private String tripartiteAccountId;

    /**
     * 三方交易单号
     */
    private String tripartiteTradeNo;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核时间
     */
    @Index
    private LocalDateTime auditTime;

    /**
     * 手续费
     */
    private Integer handlingFee;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

}