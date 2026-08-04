package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.newzkl.platform.base.biz.finance.model.purse.vo.WithdrawConfig;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.biz.finance.model.enums.AuditEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

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
    private Money amount;

    /**
     * 提货积分
     */
    private Integer goodsPoints;

    /**
     * 配置
     *
     * @ext 库中以 JSON 列存放, 走 {@code JacksonTypeHandler} 序列化; 依赖类级 {@code autoResultMap = true} 才生效
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
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