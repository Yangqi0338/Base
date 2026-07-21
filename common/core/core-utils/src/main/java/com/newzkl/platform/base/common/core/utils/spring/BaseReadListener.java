package com.newzkl.platform.base.common.core.utils.spring;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.PageReadListener;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelError;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Consumer;

/**
 * EasyExcel 模板的读取监听基类。
 *
 * @author Jiaju Zhuang
 * @param <T> 行数据类型
 */
@Slf4j
public abstract class BaseReadListener<T> extends PageReadListener<T> {

    /**
     * 错误信息汇总
     */
    @Getter
    private final EasyExcelError easyExcelError = new EasyExcelError();

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
            easyExcelError.putError(currentRowNum + "", errorMsg);
        }
        if (easyExcelError.getErrorCount() > 0) {
            return;
        }
        data = preprocess(data);

        super.invoke(data, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        super.doAfterAllAnalysed(context);

        int totalRows = context.readRowHolder().getRowIndex();
        easyExcelError.setTotalCount(totalRows);
        easyExcelError.last();
        log.info("所有数据解析完成！");
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        easyExcelError.putError(context.readRowHolder().getRowIndex() + "", exception.getMessage());
    }

    /**
     * 数据校验逻辑。
     *
     * @param rowNum 行号
     * @param data   行数据
     * @return 返回错误描述，若返回 null 或空字符串则代表校验通过
     */
    protected abstract String validate(Integer rowNum, T data);

    /**
     * 数据预处理逻辑（可选钩子）。
     *
     * @param data 原始数据
     * @return 处理后的数据
     */
    protected T preprocess(T data) {
        return data;
    }
}
