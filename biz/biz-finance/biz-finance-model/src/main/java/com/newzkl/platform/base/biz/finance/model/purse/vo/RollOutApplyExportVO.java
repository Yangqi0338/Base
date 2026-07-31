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

    /** 审核编号 */
    @ExcelProperty("审核编号")
    private Long id;

    /** 提现金额 */
    @ExcelProperty("提现金额")
    private String applyAmount;

    /** 提现手续费 */
    @ExcelProperty("提现手续费")
    private String handlingFee;

    /** 到账金额 */
    @ExcelProperty("到账金额")
    private String arrivalAmount;

    /** 审核状态 */
    @ExcelProperty("审核状态")
    private String auditState;

    /** 申请时间 */
    @ExcelProperty("申请时间")
    private LocalDateTime applyTime;
}
