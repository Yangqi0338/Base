package com.newzkl.platform.base.biz.order.action.excel;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.util.Set;

/**
 * 指定行整行填充背景色的 EasyExcel 写处理器
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.excel.RowBackGroundWriteHandler}, 行为一致:
 * 表头行不着色, 数据行按 0 基行号命中集合时整行填充。旧版用
 * {@code BooleanUtils.isNotTrue(context.getHead())} 判表头, 此处等价改为 {@code !Boolean.TRUE.equals}。</p>
 *
 * @author KC
 */
public class RowBackGroundWriteHandler implements CellWriteHandler {

    /**
     * 需要着色的行号集合 (0 基, 含表头行计数)
     */
    private final Set<Integer> highlightRowIndex;

    /**
     * 填充色索引 (取 {@code org.apache.poi.ss.usermodel.IndexedColors} 的 index)
     */
    private final short indexColors;

    /**
     * 构造着色处理器
     *
     * @param highlightRowIndex 需要着色的行号集合, 由调用方在装配导出数据时收集
     * @param indexColors       填充色索引
     */
    public RowBackGroundWriteHandler(Set<Integer> highlightRowIndex, short indexColors) {
        this.highlightRowIndex = highlightRowIndex;
        this.indexColors = indexColors;
    }

    /**
     * 单元格写出后按行号着色
     *
     * @param context 单元格写出上下文
     */
    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        if (Boolean.TRUE.equals(context.getHead())) {
            return;
        }
        if (!highlightRowIndex.contains(context.getRowIndex())) {
            return;
        }
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
        writeCellStyle.setFillForegroundColor(indexColors);
        writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
    }
}
