package com.newzkl.platform.base.biz.account.model.req;


import com.newzkl.platform.base.biz.account.model.enums.identity.ChannelEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 渠道商修改
 */
@Data
public class ChannelUpdateReq {

    /**
     * ID
     */
    private Long id;

    /**
     * 状态 (查询)
     */
    private ChannelEnum.State state;

    /**
     * 渠道商名称 (查询) channel_name
     */
    @NotNull(message = "渠道商名称不能为空")
    private String name;

    /**
     * 营业执照
     */
    private String license;

    /**
     * 联系方式
     */
    private String contactsWay;

    /**
     * 联系人
     */
    private String contactsName;

    /**
     * 企业资质信息
     */
    private CompanyInfo companyInfo;

    @Data
    public static class CompanyInfo {
        /**
         * 营业执照
         */
        private String licenseImg;
        /**
         * 企业名称
         */
        @NotNull(message = "企业名称不能为空")
        private String companyName;
        /**
         * 统一社会信用代码
         */
        private String societyCode;
        /**
         * 经营者姓名
         */
        private String manageName;
        /**
         * 经营者电话
         */
        private String managePhone;
        /**
         * 经营地址
         */
        private String workAddress;
        /**
         * 详细地址
         */
        private String detailAddress;
    }

}
