package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 保证金缴纳流水
 *
 * <p>迁移: 原 {@code com.zkl.scm.finance.model.pay.vo.PromiseFlowVO};
 * 因位于 account 对外服务签名 (IdentityService#submitPromiseFlow) 上,
 * 降级为 account 本地共享内核类型。</p>
 *
 * <p>说明: 原字段 {@code payType} 为 finance 域枚举 {@code OrderEnum.PayType},
 * 该枚举未落 account 域, 此处降级为原始编码 Integer, 不引入跨域枚举依赖。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PromiseFlowVO extends BaseRes {

    /**
     * 账号ID (查询)
     */
    private Long accountId;

    /**
     * 角色
     */
    private RoleEnum.CompanyRole role;

    /**
     * 保证金类型 (0首次/1补缴/2缓缴)
     */
    private Integer promisePayType;

    /**
     * 金额 (分)
     */
    @Positive(message = "金额必须大于0")
    private Integer amount;

    /**
     * 支付方式编码
     */
    private Integer payType;

    /**
     * 支付凭证
     */
    private String certificateUrl;
}
