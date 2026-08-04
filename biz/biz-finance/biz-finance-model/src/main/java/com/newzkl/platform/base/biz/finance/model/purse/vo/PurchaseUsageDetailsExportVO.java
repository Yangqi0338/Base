package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 账户变动记录vo
 * @date 2023/12/23 16:38
 */
@Data
public class PurchaseUsageDetailsExportVO extends BaseRes {

    /** 主键ID */
    @ExcelProperty("流水号")
    private Long id;

    /** 关联记录ID */
    @ExcelProperty("关联订单")
    private Long joinRecordId;

    /** 金额 */
    @ExcelProperty("交易金融（元）")
    private String amount;

    /**
     *
     * 变动类型 对应业务类型  1：直推
     * {@code PurseEnum.PurseAlterType}
     * {@code EarningsEnum.PurseAlterTypeEnum} 分润  1 进账 2 出账
     *
     */
    @ExcelProperty("交易类型")
    private String alterType;

    /** 时间 */
    @ExcelProperty("支付时间")
    private LocalDateTime time;

    /** 备注 */
    @ExcelProperty("备注")
    private String remark;

}
