package com.newzkl.platform.base.biz.sys.action.cmd;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * sys 域 controller 入参命令集
 *
 * <p>迁移自旧 {@code com.zkl.scm.model.web.IdObj} 等通用入参壳。字段名沿用旧命名,
 * 以免改动前端契约。</p>
 *
 * @author KC
 */
public class SysCmd {

    /**
     * 单 ID 入参 (旧 {@code IdObj})
     *
     * @author KC
     */
    @Data
    public static class ID implements Serializable {

        /**
         * 主键 ID
         */
        @NotNull(message = "id?")
        private Long id;
    }

    /**
     * Excel 导入异常明细行 (旧 {@code ExcelErrorVO.Item})
     *
     * <p>字段名与类型逐字沿用旧 {@code Item(line:Integer, msg:String)}, 表头文案与列宽同旧,
     * 以免改动前端契约。Base 的 {@code EasyExcelErrorVO.ErrorLineVO(line:String, errorMsg:String)}
     * 字段名与类型均不同, 故不复用。</p>
     *
     * @author KC
     */
    @Data
    public static class ExcelErrorItem implements Serializable {

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

    /**
     * 物流轨迹查询入参 (旧 {@code CommonCmd.DeliverQueryReq})
     *
     * <p>字段名逐字沿用旧命名, 前端契约不变</p>
     *
     * @author KC
     */
    @Data
    public static class DeliverQueryReq implements Serializable {

        /**
         * 快递公司类型编码, 不传时由三方自动识别
         */
        private String type;

        /**
         * 快递单号
         */
        private String number;

        /**
         * 收件人手机号
         */
        private String mobile;
    }
}
