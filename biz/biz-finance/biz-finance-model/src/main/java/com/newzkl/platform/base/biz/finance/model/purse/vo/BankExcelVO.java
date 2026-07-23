package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Excel 行数据映射实体。
 *
 * <p>迁移: 原 easypoi {@code @Excel} 降级为 easyexcel {@code @ExcelProperty}(index 保序)。</p>
 */
@Data
public class BankExcelVO {
    /**
     * 支行编码（对应Excel第1列：联行号）
     */
    @ExcelProperty(value = "联行号", index = 0)
    private String branchCode;

    /**
     * 支行名称（对应Excel第2列：联行名称）
     */
    @ExcelProperty(value = "联行名称", index = 1)
    private String branchName;

    /**
     * 银行编码（对应Excel第3列：总行银行号）
     */
    @ExcelProperty(value = "总行银行号", index = 2)
    private String bankCode;

    /**
     * 银行名称（对应Excel第4列：总行银行名称）
     */
    @ExcelProperty(value = "总行银行名称", index = 3)
    private String bankName;
}