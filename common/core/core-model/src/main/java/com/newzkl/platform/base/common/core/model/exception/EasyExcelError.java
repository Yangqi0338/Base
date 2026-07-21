package com.newzkl.platform.base.common.core.model.exception;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/7/2616:53
 */
@Data
public class EasyExcelError {
    /**
     * 文件名
     */
    private String filename;
    /**
     * 总数据
     */
    private Integer totalCount;
    /**
     * 成功数据
     */
    private Integer successCount;
    /**
     * 失败数据
     */
    private Integer errorCount;
    /**
     * 异常行信息
     */
    private List<ErrorLine> errorLines;
    @JsonIgnore
    private LinkedHashMap<String, List<String>> errorLineStrMap = new LinkedHashMap<>();

    public EasyExcelError() {
        this.errorLines = new ArrayList<>();
    }

    public void putError(String line, String errorMsg) {
        List<String> msgList = errorLineStrMap.computeIfAbsent(line, k -> new ArrayList<>());
        msgList.add(errorMsg);
    }

    public void last() {
        this.errorCount = this.errorLineStrMap.size();
        errorLineStrMap.forEach((line, msgList) -> {
            ErrorLine errorLine = new ErrorLine();
            errorLine.setLine(line);
            errorLine.setErrorMsg(StrUtil.join(",", msgList));
            this.errorLines.add(errorLine);
        });
        this.successCount = this.totalCount - this.errorCount;
    }

    @Data
    public static class ErrorLine {
        private String line;
        private String errorMsg;
    }


    public String getErrorMsg() {
        if (CollUtil.isEmpty(errorLines)) {
            return "";
        } else {
            return errorLines.stream().map(ErrorLine::getErrorMsg).collect(Collectors.joining("\n"));
        }
    }
}
