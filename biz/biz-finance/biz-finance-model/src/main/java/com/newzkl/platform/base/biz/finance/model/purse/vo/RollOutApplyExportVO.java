package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 转出申请记录导出
 */
@Data
public class RollOutApplyExportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty("审核编号")
    private Long id;

    @ExcelProperty("提现金额")
    private String applyAmount;

    @ExcelProperty("提现手续费")
    private String handlingFee;

    @ExcelProperty("到账金额")
    private String arrivalAmount;

    @ExcelProperty("审核状态")
    private String auditState;

    @ExcelProperty("申请时间")
    private LocalDateTime applyTime;
}
