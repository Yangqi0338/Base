package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.money.Money;

import com.baomidou.mybatisplus.annotation.*;
import com.newzkl.platform.base.biz.order.model.vo.RefundOperationRecordExt;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import lombok.Data;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

/**
 * 售后操作记录表 DO
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@Data
@TableName(autoResultMap = true)
public class RefundOperationRecordDO extends BaseDO {

    /**
     * spu订单id
     */
    private Long spuOrderId;

    /**
     * 售后单refund表的主键
     */
    private Long refundId;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作方角色编码
     */
    private AccountEnum.Identity operatorRoleCode;

    /**
     * 操作方客户端类型
     */
    private AccountEnum.Client operatorClient;

    /**
     * 操作方名称
     */
    private String operatorName;

    /**
     * 操作前售后单状态
     */
    private RefundEnum.State beforeState;

    /**
     * 操作后售后单状态
     */
    private RefundEnum.State afterState;

    /**
     * 操作类型
     */
    private com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum operationType;

    /**
     * 操作内容描述
     */
    private String operationContent;

    /**
     * 本次操作涉及的退款金额
     * @ext 落库 BIGINT 分, 与 refund 表口径一致
     */
    private Money refundAmount;

    /**
     * 物流公司名称
     */
    private String freightCompanyName;

    /**
     * 物流单号
     */
    private String freightNo;

    /**
     * 操作原因/备注
     */
    private String reason;

    /**
     * 拓展字段
     * @ext JSON 列, 对象直传
     */
    @JsonSerializable
    private RefundOperationRecordExt ext;
}