package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.RefundOperateTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 售后操作记录-视图对象（返回给前端）
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@Data
public class RefundOperationRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

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
    private RoleEnum.CompanyRole operatorRoleCode;

    /**
     * 操作方客户端类型
     * @ext 对应 CommonEnum.Client 的 code(user/supplier/channel/admin)
     */
    private String operatorClient;

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
    private RefundOperateTypeEnum operationType;

    /**
     * 操作内容描述
     */
    private String operationContent;

    /**
     * 本次操作涉及的退款金额
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
     * 操作时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 拓展字段
     * @ext JSON 结构, 存额外信息
     */
    private String ext;
}
