package com.newzkl.platform.base.biz.order.facade.model.order;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.RefundOperateTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author sijiwang
 */
@Data
@Accessors(chain = true)
public class RefundOperationRecordRPC implements Serializable {

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
     * 操作方角色编码（对应RoleEnum.CompanyRole的code：1000=C端客户，1001=供应商，1002=渠道商，0=平台等）
     */
    private RoleEnum.CompanyRole operatorRoleCode;

    /**
     * 操作方客户端类型（对应CommonEnum.Client的code：user=用户端，supplier=供应商端，channel=渠道商端，admin=平台端等）
     */
    private String operatorClient;

    /**
     * 操作方名称
     */
    private String operatorName;

    /**
     * 操作前售后单状态（对应RefundEnum.State的code）
     */
    private RefundEnum.State beforeState;

    /**
     * 操作后售后单状态（对应RefundEnum.State的code）
     */
    private RefundEnum.State afterState;

    /**
     * 操作类型（0=发起退款申请，1=审核通过，2=审核拒绝，3=提交物流信息，4=确认收货，5=退款完成，6=关闭售后，7=平台介入）
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
     * 拓展字段（JSON格式）
     */
    private String ext;

}
