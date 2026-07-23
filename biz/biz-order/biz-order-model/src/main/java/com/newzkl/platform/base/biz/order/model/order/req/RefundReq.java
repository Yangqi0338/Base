package com.newzkl.platform.base.biz.order.model.order.req;

import com.newzkl.platform.base.biz.order.model.enums.user.identity.RoleEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 售后单
 * @author fang
 */
@Data
public class RefundReq {
    /**
     * 售后类型 0 仅退款 1 退货退款
     */
    @NotNull
    private Integer refundType;
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
     * SPU订单ID
     */
    @NotNull(message = "spuOrderNo不能为空")
    private String spuOrderNo;
    /**
     * 订单ID
     */
    @NotNull(message = "orderNo不能为空")
    private String orderNo;
    /**
     * 售后明细
     */
    @NotEmpty
    @Valid
    private List<RefundItemReq> refundItemCommandList;
    /**
     * 申请角色
     */
    private RoleEnum.CompanyRole role;
}
