package com.newzkl.platform.base.common.core.office;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;

import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * EasyExcel 导入 / 导出封装工具
 *
 * @author fang
 */
public class EasyExcelUtil {

    private static <T> BaseReadListener<T> createListener(BiFunction<T, Integer, String> validateFunc, Consumer<List<T>> saveDataFunc) {
        return new BaseReadListener<>(saveDataFunc) {
            @Override
            protected String validate(Integer rowNum, T data) {
                String result = validateFunc.apply(data, rowNum);
                if (StrUtil.isBlank(result)) {
                    String validateStr = BizUtil.getValidateStr(data, ";");
                    if (StrUtil.isBlank(validateStr)) {
                        return getMessage(rowNum, validateStr);
                    }
                }
                return result;
            }
        };
    }

    public static String getMessage(Integer rowNum, String msg) {
        return StrUtil.format("第{}行：{} 跳过导入", rowNum, msg);
    }

    public static <T> EasyExcelErrorVO importBiz(InputStream inputStream, Class<T> clazz,
                                                 Consumer<List<T>> saveDataFunc) {
        return importBiz(inputStream, clazz, (data, rowNum) -> "", saveDataFunc, null);
    }


    public static <T> EasyExcelErrorVO importBiz(InputStream inputStream, Class<T> clazz,
                                                 Consumer<List<T>> saveDataFunc,
                                                 ImportParam importParam) {
        return importBiz(inputStream, clazz, (data, rowNum) -> "", saveDataFunc, importParam);
    }

    public static <T> EasyExcelErrorVO importBiz(InputStream inputStream, Class<T> clazz,
                                                 BiFunction<T, Integer, String> validateFunc,
                                                 Consumer<List<T>> saveDataFunc) {
        return importBiz(inputStream, clazz, validateFunc, saveDataFunc, null);
    }

    public static <T> EasyExcelErrorVO importBiz(InputStream inputStream, Class<T> clazz,
                                                 BiFunction<T, Integer, String> validateFunc,
                                                 Consumer<List<T>> saveDataFunc,
                                                 ImportParam importParam) {
        BaseReadListener<T> listener = createListener(validateFunc, saveDataFunc);
        ExcelReaderBuilder builder = EasyExcel.read(inputStream, clazz, listener);
        ImportParam.build(importParam, builder).doRead();
        return listener.buildResult();
    }

    @Data
    @Accessors(chain = true)
    public static class ImportParam {
        private Integer sheetNo;
        private String sheetName;
        private Integer headRowNum;

        private static ExcelReaderSheetBuilder build(ImportParam importParam, ExcelReaderBuilder builder) {
            ExcelReaderSheetBuilder sheetBuilder = null;
            if (importParam != null) {
                if (importParam.sheetNo != null) {
                    sheetBuilder = builder.sheet(importParam.sheetNo);
                }
                if (StrUtil.isNotBlank(importParam.sheetName)) {
                    sheetBuilder = builder.sheet(importParam.sheetName);
                }
            }

            if (sheetBuilder == null) {
                sheetBuilder = builder.sheet();
            }

            if (importParam != null) {
                sheetBuilder.headRowNumber(importParam.headRowNum);
            }
            return sheetBuilder;
        }
    }

    public static HttpServletResponse findResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new RuntimeException("当前无HTTP请求上下文");
        }

        HttpServletResponse response = attributes.getResponse();
        if (response == null) {
            throw new RuntimeException("无法获取HttpServletResponse");
        }
        return response;
    }

    public static <T> void export(List<T> exportResponses, String exportFileName) throws IOException {
        export(exportResponses, null, exportFileName);
    }

    public static <T> void export(List<T> exportResponses, WriteHandler handler, String exportFileName) throws IOException {
        if (CollUtil.isEmpty(exportResponses)) return;

        HttpServletResponse response = findResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(exportFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        ExcelWriterBuilder builder = EasyExcel.write(response.getOutputStream(), exportResponses.get(0).getClass());
        if (handler != null) {
            builder.registerWriteHandler(handler);
        }
        builder.sheet("模板").doWrite(exportResponses);
    }
}
