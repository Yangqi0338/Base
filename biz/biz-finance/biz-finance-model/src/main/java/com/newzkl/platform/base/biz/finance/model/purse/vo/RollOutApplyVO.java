package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 转出申请记录
 * @date 2023/12/23 11:45
 */
@Data
public class RollOutApplyVO extends BaseRes {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 转出申请账户类型
     */
    private PurseEnum.PurseType purseType;

    /**
     * 申请金额
     */
    private Money applyAmount;

    /**
     * 审核状态 0：待审核  1：审核通过  2：审核拒绝
     */
    private AuditEnum.State auditState;

    /**
     * 三方交易状态 0：未成功  1：成功
     */
    private Integer tripartiteTradeState;

    /**
     * 三方用户id
     */
    private String tripartiteAccountId;

    /**
     * 三方支付交易单号
     */
    private String tripartiteTradeNo;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 手续费
     */
    private Money handlingFee;

}
