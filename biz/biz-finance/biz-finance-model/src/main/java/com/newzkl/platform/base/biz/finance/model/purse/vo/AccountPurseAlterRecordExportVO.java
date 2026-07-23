package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 账户变动记录vo
 * @date 2023/12/23 16:38
 */
@Data
public class AccountPurseAlterRecordExportVO extends BaseRes {

    @ExcelProperty("流水号")
    private Long id;

    @ExcelProperty("关联单号")
    private Long joinRecordId;

    @ExcelProperty("交易金融（元）")
    private String amount;

    /**
     *
     * 变动类型 对应业务类型  1：直推
     * {@link PurseEnum.PurseAlterType}
     * {@link EarningsEnum.PurseAlterTypeEnum} 分润  1 进账 2 出账
     *
     */
    @ExcelProperty("交易类型")
    private String alterType;

    @ExcelProperty("交易时间")
    private LocalDateTime time;

    @ExcelProperty("备注")
    private String remark;

}
