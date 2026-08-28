package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 资质信息详细字段
 *
 * <p>供应商/渠道商角色申请时提交的企业与经营者资质资料, 纯数据载体, 不含账号标识。
 * 迁移自 {@code com.zkl.scm.user.domain.role.model.vo.CompanyInfoVO}, 结构透出前端契约</p>
 *
 * @author KC
 */
@Data
public class CompanyInfoVO implements Serializable {

    /**
     * 企业主体
     */
    private String companyBody;

    /**
     * 营业执照
     */
    private String licenseImg;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 统一社会信用代码
     */
    private String societyCode;

    /**
     * 法定代表人
     */
    private String legalName;

    /**
     * 注册资本
     */
    private String registerAmount;

    /**
     * 成立日期
     */
    private String createTime;

    /**
     * 经营范围
     */
    private String businessRange;

    /**
     * 法人证件人像面照片
     */
    private String cardUpImg;

    /**
     * 法人证件国徽面照片
     */
    private String cardDownImg;

    /**
     * 法人证件号
     */
    private String legalCardNumber;

    /**
     * 法人证件有效期: 长期有效
     */
    private String isLegalCardTimeLong;

    /**
     * 法人证件有效期开始时间
     */
    private String legalCardTimeStart;

    /**
     * 法人证件有效期结束时间
     */
    private String legalCardTimeEnd;

    /**
     * 法人证件居住地址
     */
    private String legalCardAddress;

    /**
     * 法人联系方式
     */
    private String legalPhone;

    /**
     * 经营模式
     */
    private String manageType;

    /**
     * 经营行业id集合
     */
    private String manageIndustryIdList;

    /**
     * 经营行业
     */
    private String manageIndustryNameList;

    /**
     * 经营者姓名
     */
    private String manageName;

    /**
     * 经营者证件人像面照片
     */
    private String cardWorkUpImg;

    /**
     * 经营者证件国徽面照片
     */
    private String cardWorkDownImg;

    /**
     * 经营者证件号
     */
    private String cardNumber;

    /**
     * 经营者证件居住地址
     */
    private String cardAddress;

    /**
     * 经营者证件有效期
     */
    private String isCardTimeLong;

    /**
     * 证件有效期开始时间
     */
    private String cardTimeStart;

    /**
     * 证件有效期结束时间
     */
    private String cardTimeEnd;

    /**
     * 经营者电话
     */
    private String managePhone;

    /**
     * 经营者邮箱
     */
    private String manageMail;

    /**
     * 经营地址
     */
    private String workAddress;

    /**
     * 其他资质
     */
    private String dataGroups;

    /**
     * 详细地址
     */
    private String detailAddress;
}
