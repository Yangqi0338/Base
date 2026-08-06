package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
// TODO[pom-gap mybatis-plus-ext]: import org.dromara.mpe.autofill.annotation.JsonSerializable; (annotation 依赖延迟补)

import java.time.LocalDateTime;

/**
 * @author 分润记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class EarningRecordDO extends BaseDO {

    /**
     * 消费类型
     */
    @Index
    private EarningsEnum.EarningType earningType;

    /**
     * 分润金额
     */
    private Money amount;

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
     * 贡献对象id
     */
    @Index
    private Long contributeId;

    /**
     * 贡献对象id
     */
    private String contributeName;

    /**
     * 关联订单
     */
    @Index
    private Long joinOrderNo;

    /**
     * 关联交易单号（源 earning_record.join_trade_no 列）
     */
    private Long joinTradeNo;

    /**
     * 分润角色 id（源 earning_record.role 列，列名 role 为保留字故显式指定）
     */
    @TableField("role")
    private Long roleId;

    /**
     * 商品信息
     */
    // TODO[pom-gap mybatis-plus-ext]: @JsonSerializable (autofill json, 依赖延迟补)
    private String goodsInfo;

    /**
     * 分润时间
     */
    @Index
    private LocalDateTime earningTime;

    /**
     * 状态
     */
    private EarningsEnum.State state;
}