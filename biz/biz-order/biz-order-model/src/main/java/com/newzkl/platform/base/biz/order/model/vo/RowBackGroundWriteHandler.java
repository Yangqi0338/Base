package com.newzkl.platform.base.biz.order.model.vo;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.BooleanUtils;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import java.util.Set;
 
/**
 * Set background color
 * @author liaoguang
 */
public class RowBackGroundWriteHandler implements CellWriteHandler {
 
    /**
     * Set background color row index set
     */
    private final Set<Integer> yellowRowIndex;
    /**
     * The background color that needs to be set
     */
    private final short indexColors;
 
    public RowBackGroundWriteHandler(Set<Integer> yellowRowIndex, short indexColors) {
        this.yellowRowIndex = yellowRowIndex;
        this.indexColors = indexColors;
    }
 
    @Override
    public void beforeCellCreate(CellWriteHandlerContext context) {
        // Row in source code context to be optimized: supports parameter input to obtain the number of
        // occurrences of the required field and set the background color
        // to do：（row.getCell(columnIndex)lose efficacy）
    }
 
    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        if (BooleanUtils.isNotTrue(context.getHead())) {
            // get current row index
            Integer currentRowIndex = context.getRowIndex();
            // judge yellowRowIndex's
            if (yellowRowIndex.contains(currentRowIndex)) {
                WriteCellData<?> cellData = context.getFirstCellData();
                WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                // set index color
                writeCellStyle.setFillForegroundColor(indexColors);
                writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            }
        }
    }
 
}