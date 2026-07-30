package com.newzkl.platform.base.common.core.utils.spring;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.PageReadListener;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * EasyExcel 模板的读取监听基类
 *
 * <p>内部以可变结构累加校验异常, 解析完成后一次性构建不可变结果 {@code EasyExcelErrorVO}。</p>
 *
 * @param <T> 行数据类型
 * @author Jiaju Zhuang
 */
@Slf4j
public abstract class BaseReadListener<T> extends PageReadListener<T> {

    /**
     * 异常行累加器: 行号 -> 该行错误信息列表
     */
    private final LinkedHashMap<String, List<String>> errorLineStrMap = new LinkedHashMap<>();

    /**
     * 总行数
     */
    private int totalCount;

    public BaseReadListener(Consumer<List<T>> consumer) {
        super(consumer);
    }

    public BaseReadListener(Consumer<List<T>> consumer, int batchCount) {
        super(consumer, batchCount);
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        Integer currentRowNum = context.readRowHolder().getRowIndex();
        String errorMsg = validate(currentRowNum, data);
        if (StrUtil.isNotBlank(errorMsg)) {
            putError(currentRowNum + "", errorMsg);
        }
        if (!errorLineStrMap.isEmpty()) {
            return;
        }
        data = preprocess(data);

        super.invoke(data, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        super.doAfterAllAnalysed(context);

        this.totalCount = context.readRowHolder().getRowIndex();
        log.info("所有数据解析完成！");
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        putError(context.readRowHolder().getRowIndex() + "", exception.getMessage());
    }

    private void putError(String line, String errorMsg) {
        errorLineStrMap.computeIfAbsent(line, k -> new ArrayList<>()).add(errorMsg);
    }

    /**
     * 构建不可变的导入结果
     *
     * @return Excel 导入结果载体
     */
    public EasyExcelErrorVO buildResult() {
        List<EasyExcelErrorVO.ErrorLineVO> errorLines = new ArrayList<>();
        errorLineStrMap.forEach((line, msgList) ->
                errorLines.add(new EasyExcelErrorVO.ErrorLineVO(line, StrUtil.join(",", msgList))));
        int errorCount = errorLineStrMap.size();
        return new EasyExcelErrorVO(null, totalCount, totalCount - errorCount, errorCount, errorLines);
    }

    /**
     * 数据校验逻辑
     *
     * @param rowNum 行号
     * @param data   行数据
     * @return 返回错误描述，若返回 null 或空字符串则代表校验通过
     */
    protected abstract String validate(Integer rowNum, T data);

    /**
     * 数据预处理逻辑（可选钩子）
     *
     * @param data 原始数据
     * @return 处理后的数据
     */
    protected T preprocess(T data) {
        return data;
    }
}
