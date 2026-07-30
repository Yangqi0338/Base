package com.newzkl.platform.base.biz.account.model.res;

import lombok.Data;

/**
 * 汇总单项
 *
 * <p>迁自旧 {@code com.zkl.scm.user.application.model.res.summary.SummaryVO}。
 * 字段名 {@code dimensionValue} / {@code amount} 逐字沿用, 不改前端契约</p>
 *
 * @author KC
 */
@Data
public class SummaryRes {

    /**
     * 维度值
     */
    private String dimensionValue;

    /**
     * 数值
     */
    private Integer amount;
}
