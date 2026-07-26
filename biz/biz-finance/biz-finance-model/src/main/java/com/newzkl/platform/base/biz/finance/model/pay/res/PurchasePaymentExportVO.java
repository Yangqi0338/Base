package com.newzkl.platform.base.biz.finance.model.pay.res;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 采购金充值记录导出视图。
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.pay.model.vo.PurchasePaymentExportVO}。
 * 金额已由分转元 (保留两位小数), 支付状态已由枚举翻译为中文, 故均为字符串。</p>
 *
 * @author KC
 */
@Data
public class PurchasePaymentExportVO {

    /**
     * 交易单号。
     *
     * <p>表头沿用旧实现 (旧代码此列映射的是 accountId, 保持不变以免破坏既有导出模板)。</p>
     */
    @ExcelProperty("交易单号")
    private Long accountId;

    /**
     * 支付金额 (元, 两位小数)。
     */
    @ExcelProperty("支付金额")
    private String payAmount;

    /**
     * 支付状态中文描述。
     */
    @ExcelProperty("支付状态")
    private String payState;

    /**
     * 支付时间。
     */
    @ExcelProperty("支付时间")
    private LocalDateTime payTime;
}
