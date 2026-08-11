package com.newzkl.platform.base.biz.socialbang.model.event.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SettleDetailDataListVO {


    /**
     * 流水号
     */
    @ExcelProperty("流水号")
    @ColumnWidth(value = 30)
    private String serialId;


    /**
     * 昵称
     */
    @ExcelProperty("昵称")
    @ColumnWidth(value = 20)
    private String nickName;


    /**
     * 角色名称
     */
    @ExcelProperty("角色名称")
    @ColumnWidth(value = 20)
    private String roleName;



    /**
     * 手机号
     */
    @ExcelProperty("手机号")
    @ColumnWidth(value = 20)
    private String phone;


    /**
     * 个人比例
     */
    @ExcelProperty("个人比例")
    @ColumnWidth(value = 20)
    private BigDecimal personPercent;


    /**
     * 分红奖金
     */
    @ExcelProperty("分红奖金")
    @ColumnWidth(value = 20)
    private BigDecimal dividendAmount;


    /**
     * 结算周期
     */
    @ExcelProperty("结算周期")
    @ColumnWidth(value = 20)
    private String dividendCycle;



}
