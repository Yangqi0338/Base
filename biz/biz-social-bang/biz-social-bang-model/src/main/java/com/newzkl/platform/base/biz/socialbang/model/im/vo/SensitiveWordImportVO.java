package com.newzkl.platform.base.biz.socialbang.model.im.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 敏感词导入 Excel 行对象
 *
 * <p>迁移自 {@code com.zkl.scm.im.base.tencent.vo.SensitiveWordImportVO}。
 * 源用 easypoi {@code @Excel(name = "敏感词（必填）", orderNum = "0")};
 * Base 统一 EasyExcel, 换为 {@code @ExcelProperty}, 列名与列序不变。</p>
 *
 * @author KC
 */
@Data
public class SensitiveWordImportVO {

    /**
     * 敏感词(必填), 对应模板 A 列
     */
    @ExcelProperty(value = "敏感词（必填）", index = 0)
    private String sensitiveWord;
}
