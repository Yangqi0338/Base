package com.newzkl.platform.base.biz.order.model.order.req;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author sijiwang
 */
@Data
public class CancelOrderReq {

    /**
     * 订单号
     */
    @NotNull(message = "订单号不能为空")
    private String orderNo;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 角色
     */
    private RoleEnum.CompanyRole companyRole;
}
