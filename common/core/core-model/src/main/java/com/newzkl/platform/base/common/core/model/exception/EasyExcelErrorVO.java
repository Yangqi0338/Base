package com.newzkl.platform.base.common.core.model.exception;

import cn.hutool.core.collection.CollUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Excel 导入结果载体。
 *
 * <p>不可变结果对象, 由导入监听器累加完成后一次性构建;
 * 累加逻辑位于 core-utils 的 BaseReadListener。</p>
 *
 * @param filename     文件名
 * @param totalCount   总行数
 * @param successCount 成功行数
 * @param errorCount   失败行数
 * @param errorLines   异常行明细
 * @author fang
 */
public record EasyExcelErrorVO(
        String filename,
        Integer totalCount,
        Integer successCount,
        Integer errorCount,
        List<ErrorLineVO> errorLines) {

    /**
     * 异常行明细。
     *
     * @param line     行号
     * @param errorMsg 错误信息
     */
    public record ErrorLineVO(String line, String errorMsg) {
    }

    /**
     * 汇总全部异常行信息为多行文本。
     *
     * @return 以换行拼接的错误描述, 无错误时返回空串
     */
    public String errorMsg() {
        if (CollUtil.isEmpty(errorLines)) {
            return "";
        }
        return errorLines.stream().map(ErrorLineVO::errorMsg).collect(Collectors.joining("\n"));
    }
}
