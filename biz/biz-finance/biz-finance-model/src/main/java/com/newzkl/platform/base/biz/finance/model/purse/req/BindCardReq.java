package com.newzkl.platform.base.biz.finance.model.purse.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 绑卡请求
 *
 * @author niu
 * @description:
 * @date 2025-08-25 17:05:40
 */
@Data
public class BindCardReq {

    /**
     * 卡类型 0：对公，1：对私法人，4：对公非同名；
     */
    @NotBlank(message = "卡类型不能为空")
    private String cardType;

    /**
     * 持卡人姓名
     */
    @NotBlank(message = "持卡人姓名不能为空")
    private String cardName;

    /**
     * 卡号
     */
    @NotBlank(message = "卡号不能为空")
    private String cardNo;

    /**
     * 卡绑定的手机号
     */
    @NotBlank(message = "卡绑定的手机号不能为空")
    private String cardMobile;

    /**
     * 银行编号
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 对公支行联行号
     */
    @Size(max = 12, message = "对公支行联行号长度不能超过12位")
    private String branchCode;

    /**
     * 银行地址(省)
     */
    @NotBlank(message = "银行地址(省)不能为空")
    private String bankProvCode;

    /**
     * 银行地址(市)
     */
    @NotBlank(message = "银行地址(市)不能为空")
    private String bankAreaCode;

    /**
     * 身份证号码
     */
    private String certNo;

    /**
     * 证件有效期类型 1：长期有效；0：非长期有效；
     */
    private String certValidType;

    /**
     * 证件有效期开始日期 日期格式：yyyyMMdd
     */
    private String certBeginDate;

    /**
     * 证件有效期截止日期 日期格式：yyyyMMdd
     */
    private String certEndDate;

}
