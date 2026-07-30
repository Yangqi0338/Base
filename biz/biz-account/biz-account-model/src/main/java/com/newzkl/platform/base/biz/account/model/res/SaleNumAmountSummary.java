package com.newzkl.platform.base.biz.account.model.res;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import java.util.List;

/**
 * 交易趋势汇总
 *
 * <p>迁自旧 {@code com.zkl.scm.user.interfaces.controller.model.res.SaleNumAmountSummary}。
 * 字段名与类型 (含 {@code startDate} / {@code endDate} 用 hutool {@code DateTime})
 * 逐字沿用, 不改前端契约</p>
 *
 * @author KC
 */
@Data
public class SaleNumAmountSummary {

    /**
     * 汇总开始时间
     */
    private DateTime startDate;

    /**
     * 汇总结束时间
     */
    private DateTime endDate;

    /**
     * 维度
     */
    private String dimension;

    /**
     * 数量
     */
    private List<SummaryRes> num;

    /**
     * 销售额
     */
    private List<SummaryRes> amount;
}
