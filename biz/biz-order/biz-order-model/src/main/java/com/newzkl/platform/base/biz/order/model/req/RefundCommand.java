package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 售后单
 * @author fang
 */
@Data
public class RefundCommand {
    /**
     * 售后类型 0 仅退款 1 退货退款
     */
    @NotNull
     private RefundEnum.RefundType refundType;
    /**
     * 售后原因
     */
    private String reason;
    /**
     * 申请说明
     */
    private String remark;
    /**
     * 申请图片
     */
    private String images;
    /**
     * 订单号
     */
    @NotNull
    private String orderNo;
    /**
     * 售后明细
     */
    @NotEmpty
    @Valid
    private List<RefundItemCommand> refundItemCommandList;
    /**
     * 申请角色
     */
    private AccountEnum.Identity identity;
}
