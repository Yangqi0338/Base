package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 售后操作记录表 DO。
 *
 * <p>迁移补充: 原 {@code spu_order_id} (Long) 收敛为业务单号 {@code spu_order_no} (String),
 * 与 {@code OrderStateRecordDO} / {@code SettleOrderWaitDO} / {@code RefundOperationRecordVO} 对齐。</p>
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("refund_operation_record")
public class RefundOperationRecordDO extends BaseDO {

    /**
     * SPU 订单号
     */
    @Index
    private String spuOrderNo;

    /**
     * 售后单 refund 表的主键
     */
    @Index
    private Long refundId;

    /**
     * 操作人 ID
     */
    private Long operatorId;

    /**
     * 操作方角色编码 (对应 RoleEnum.CompanyRole 的 code: 1000=C端客户, 1001=供应商, 1002=渠道商, 0=平台等)
     */
    private Long operatorRoleCode;

    /**
     * 操作方客户端类型 (对应 CommonEnum.Client 的 code: user=用户端, supplier=供应商端, channel=渠道商端, admin=平台端等)
     */
    private String operatorClient;

    /**
     * 操作方名称
     */
    private String operatorName;

    /**
     * 操作前售后单状态 (对应 RefundEnum.State 的 code)
     */
    private RefundEnum.State beforeState;

    /**
     * 操作后售后单状态 (对应 RefundEnum.State 的 code)
     */
    private RefundEnum.State afterState;

    /**
     * 操作类型 (0=发起退款申请, 1=审核通过, 2=审核拒绝, 3=提交物流信息, 4=确认收货, 5=退款完成, 6=关闭售后, 7=平台介入)
     */
    @Index
    private Integer operationType;

    /**
     * 操作内容描述
     */
    private String operationContent;

    /**
     * 本次操作涉及的退款金额 (单位: 分, 和 refund 表保持一致)
     */
    private Integer refundAmount;

    /**
     * 物流公司名称
     */
    private String freightCompanyName;

    /**
     * 物流单号
     */
    @Index
    private String freightNo;

    /**
     * 操作原因/备注
     */
    private String reason;

    /**
     * 拓展字段 (JSON 格式存储额外信息)
     */
    private String ext;
}
