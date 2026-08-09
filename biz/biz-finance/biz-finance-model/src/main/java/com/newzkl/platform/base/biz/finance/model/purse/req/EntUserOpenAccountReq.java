package com.newzkl.platform.base.biz.finance.model.purse.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 企业用户开户请求
 *
 * @author niu
 * @date 2025-08-25 17:03:02
 */
@Data
public class EntUserOpenAccountReq {

    /**
     * 企业用户名称
     */
    @NotBlank(message = "企业用户名称不能为空")
    private String entName;

    /**
     * 营业执照编号
     */
    @NotBlank(message = "营业执照编号不能为空")
    private String licenseCode;

    /**
     * 证照有效期类型
     * @ext 1 长期有效 0 非长期有效; 无对应枚举, 保留 String
     */
    @NotBlank(message = "证照有效期类型不能为空")
    private String licenseValidType;

    /**
     * 证照有效期起始日期
     * @ext 日期格式 yyyyMMdd
     */
    @NotBlank(message = "证照有效期起始日期不能为空")
    private String licenseBeginDate;

    /**
     * 证照有效期结束日期
     * @ext 日期格式 yyyyMMdd
     */
    private String licenseEndDate;

    /**
     * 企业地址(省)
     */
    @NotBlank(message = "企业地址(省)不能为空")
    private String entProvCode;

    /**
     * 企业地址(市)
     */
    @NotBlank(message = "企业地址(市)不能为空")
    private String entAreaCode;

    /**
     * 企业地址(区)
     */
    @NotBlank(message = "企业地址(区)不能为空")
    private String entRegionCode;

    /**
     * 企业地址(详细地址)
     */
    @NotBlank(message = "企业地址(详细地址)不能为空")
    private String entAddress;

    /**
     * 法人姓名
     */
    @NotBlank(message = "法人姓名不能为空")
    private String legalName;

    /**
     * 法人证件类型
     * @ext 00 身份证; 无对应枚举, 保留 String
     */
    @NotBlank(message = "法人证件类型不能为空")
    private String legalCertType;

    /**
     * 法人证件号码
     */
    @NotBlank(message = "法人证件号码不能为空")
    private String legalCertNo;

    /**
     * 法人证件有效期类型
     * @ext 1 长期有效 0 非长期有效; 无对应枚举, 保留 String
     */
    @NotBlank(message = "法人证件有效期类型不能为空")
    private String legalCertValidType;

    /**
     * 法人证件有效期开始日期
     * @ext 日期格式 yyyyMMdd
     */
    @NotBlank(message = "法人证件有效期开始日期不能为空")
    private String legalCertBeginDate;

    /**
     * 法人证件有效期截止日期
     * @ext 日期格式 yyyyMMdd
     */
    private String legalCertEndDate;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    /**
     * 联系人手机号
     */
    @NotBlank(message = "联系人手机号不能为空")
    private String contactMobile;
}
