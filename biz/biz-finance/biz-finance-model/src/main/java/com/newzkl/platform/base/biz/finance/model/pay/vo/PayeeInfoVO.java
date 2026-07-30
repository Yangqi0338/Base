package com.newzkl.platform.base.biz.finance.model.pay.vo;

import lombok.Data;

/**
 * 现金流收款方配置
 *
 * <p>迁移自 new-scm {@code domain.pay.model.vo.PayeeInfoVO}。</p>
 *
 * @author KC
 */
@Data
public class PayeeInfoVO {

    /**
     * 收款方标识。收款方为用户时取用户 user_id, 为平台商户时取平台商户号。
     */
    private String payeeId;

    /**
     * 收款方类型。用户: {@code USER}; 平台商户: {@code MERCHANT}。
     */
    private String payeeType;

    /**
     * 收款比例
     */
    private Integer payeeRatio;

    /**
     * 收款方名称
     */
    private String payeeName;
}
