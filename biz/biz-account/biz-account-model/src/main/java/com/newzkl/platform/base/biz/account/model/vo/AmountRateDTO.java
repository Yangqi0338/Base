package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 金额-费率阶梯项。
 *
 * <p>迁移: 原 {@code com.zkl.scm.finance.model.earnings.res.AmountRateDTO};
 * 因位于 account 对外服务签名 (ServiceFeeConfigVO) 上, 降级为 account 本地共享内核类型。</p>
 *
 * @author KC
 */
@Data
public class AmountRateDTO implements Serializable {

    /**
     * 阶梯金额 (分)
     */
    private Integer amount;

    /**
     * 费率
     */
    private Double rate;
}
