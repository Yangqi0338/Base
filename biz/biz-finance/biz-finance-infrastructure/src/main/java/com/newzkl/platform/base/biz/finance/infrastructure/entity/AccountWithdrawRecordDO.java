package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.finance.model.purse.vo.WithdrawConfig;
import com.newzkl.platform.base.common.ddd.model.BaseDO;
import com.newzkl.platform.base.biz.finance.model.enums.AuditEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
// TODO[pom-gap mybatis-plus-ext]: import org.dromara.mpe.autofill.annotation.JsonSerializable; (annotation 依赖延迟补)

/**
 * @author 提现记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
public class AccountWithdrawRecordDO extends BaseDO {

    /**
     * 客户id
     */
    @Index
    private Long accountId;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 提货积分
     */
    private Integer goodsPoints;

    /**
     * 配置
     */
    // TODO[pom-gap mybatis-plus-ext]: @JsonSerializable (autofill json, 依赖延迟补)
    private WithdrawConfig config;

    /**
     * 提现时间
     */
    @Index
    private String finishTime;

    /**
     * 三方交易单号
     */
    private String tripartiteTradeNo;

    /**
     * 状态
     */
    @Index
    private AuditEnum.WithdrawSate state;
}