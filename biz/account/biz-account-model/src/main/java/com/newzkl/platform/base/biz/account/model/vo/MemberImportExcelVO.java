package com.newzkl.platform.base.biz.account.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 会员导入Excel PO类（对应导入模板列）
 *
 * @author sijiwang
 */
@Data
public class MemberImportExcelVO {

    /**
     * 手机号（必填）- 对应模板A列
     */
    @ExcelProperty("手机号（必填）")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 用户昵称（选填，默认手机号）- 对应模板B列
     */
    @ExcelProperty("用户昵称（不填默认使用手机号）")
    private String nickname;
}
