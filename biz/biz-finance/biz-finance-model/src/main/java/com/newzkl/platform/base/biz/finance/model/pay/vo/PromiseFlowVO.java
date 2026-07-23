package com.newzkl.platform.base.biz.finance.model.pay.vo;


import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.finance.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.finance.model.enums.user.identity.RoleEnum;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * 保证金流水
 *
 * @author fang
 */
@Data
public class PromiseFlowVO extends BaseRes {

    /**
     * 账号ID (查询)
     */
    private Long accountId;
    /**
     * 角色ID
     */
    private RoleEnum.CompanyRole role;
    /**
     * 保证金类型（0首次/1补缴/2缓缴）
     */
    private Integer promisePayType;
    /**
     * 金额
     */
    @Positive(message = "金额必须大于0")
    private Integer amount;
    /**
     * 支付方式
     */
    private OrderEnum.PayType payType;
    /**
     * 支付凭证
     */
    private String certificateUrl;
}