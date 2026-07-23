package com.newzkl.platform.base.biz.finance.model.purse.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户开户请求
 *
 * @author niu
 * @description:
 * @date 2025-08-25 16:58:54
 */
@Data
public class UserOpenAccountReq {
    /**
     * 用户名称
     */
    @NotBlank(message = "用户名称不能为空")
    private String name;

    /**
     * 证件类型 00 身份证号
     */
    @NotBlank(message = "证件类型不能为空")
    private String certType;

    /**
     * 证件号码
     */
    @NotBlank(message = "证件号码不能为空")
    private String certNo;

    /**
     * 证件有效期类型 1:长期有效 0:非长期有效；
     */
    @NotBlank(message = "证件有效期类型不能为空")
    private String certValidType;

    /**
     * 证件有效期开始日期 日期格式：yyyyMMdd；
     */
    @NotBlank(message = "证件有效期开始日期不能为空")
    private String certBeginDate;

    /**
     * 证件有效期截止日期 日期格式：yyyyMMdd；
     */
    private String certEndDate;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String mobile;
}
