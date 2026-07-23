package com.newzkl.platform.base.biz.order.model.order.req;

import com.newzkl.platform.base.biz.order.model.enums.user.identity.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 确认订单
 * @author sijiwang
 */
@Data
public class ConfirmOrderReq {

    /**
     * 订单号
     */
    @NotNull(message = "订单号不能为空")
    private String orderNo;

    private RoleEnum.CompanyRole companyRole;
}
