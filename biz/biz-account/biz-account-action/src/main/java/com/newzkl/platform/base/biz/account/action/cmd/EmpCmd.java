package com.newzkl.platform.base.biz.account.action.cmd;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工接口入参命令集
 *
 * <p>迁移说明: 旧 {@code excelCreateEmp} 收通用包装类 {@code com.zkl.scm.model.web.StringObj},
 * Base 通用层未提供对等类型, 故在本域 action 层内聚。字段名 {@code string} 逐字沿用,
 * 不改前端契约</p>
 *
 * @author KC
 */
public class EmpCmd {

    /**
     * Excel 地址入参 (对应旧 {@code StringObj})
     *
     * @author KC
     */
    @Data
    public static class ExcelUrl implements Serializable {

        /**
         * Excel 文件地址
         */
        @NotEmpty(message = "文件地址不能为空")
        private String string;
    }
}
