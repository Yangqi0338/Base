package com.newzkl.platform.base.biz.order.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelErrorVO implements Serializable {
    /**
     * 总数
     */
    private Integer total;
    /**
     * 成功数
     */
    private Integer success;
    /**
     * 异常明细
     */
    private List<Item> error;

    public ExcelErrorVO(int total){
        this.total = total;
        this.success = total;
    }
    public void addError(Integer line, String msg){
        Item item = new Item(line, msg);
        if(this.error == null){
            this.error = new ArrayList<>();
        }
        this.error.add(item);
        this.success = this.success - 1;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item implements Serializable {
        /**
         * 行号
         */
        @ExcelProperty("行号")
        @ColumnWidth(value = 15)
        private Integer line;
        /**
         * 异常信息
         */
        @ExcelProperty("异常信息")
        @ColumnWidth(value = 80)
        private String msg;
    }
}
