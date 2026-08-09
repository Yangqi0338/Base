package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 销售统计持久化对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class CountSaleDO extends BaseDO {

    /**
     * 角色编码
     * @ext 旧表以字符串存储角色码
     */
    @Index
    private String role;

    /**
     * 账号 ID
     */
    @Index
    private Long accountId;

    /**
     * 统计日期
     */
    @Index
    private LocalDateTime date;

    /**
     * 累计订单数
     */
    private Integer totalOrderNumber;

    /**
     * 累计订单金额
     * @ext Money, 落库 BIGINT 分
     */
    private Money totalOrderAmount;

    /**
     * 累计退款数
     */
    private Integer totalRefundNumber;

    /**
     * 累计退款金额
     * @ext Money, 落库 BIGINT 分
     */
    private Money totalRefundAmount;

    /**
     * 贡献金额
     * @ext Money, 落库 BIGINT 分
     */
    private Money contributeAmount;
}
