package com.newzkl.platform.base.biz.auth.model.oauth.req;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 身份注册请求参数
 *
 * @author muc_fang
 * @date 2024/2/22 11:22
 */
@Data
public class IdentitySaveReq extends AccountSaveReq {

    /**
     * 联系方式
     */
    private String contactsWay;

    /**
     * 渠道商名称
     */
    private String name;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 联系人
     */
    private String contactsName;

    /**
     * 企业资质信息
     */
    private CompanyInfo companyInfo;
    /**
     * 营业执照
     */
    private String license;


    //====================== 新增IM/系统用户相关字段 =====================

    /**
     * 数字门店权限
     */
    private CommonEnum.YesOrNo storePermission;

    /**
     * 创建用户
     */
    private CommonEnum.YesOrNo createMember;

    /**
     * 分润比例
     */
    private Double serviceRate;

    /**
     * 企业实名信息
     */
    @Data
    @NoArgsConstructor
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

        /**
         * 按企业名称构造
         *
         * @param companyName 企业名称
         */
        public CompanyInfo(String companyName) {
            this.companyName = companyName;
        }
    }

}
