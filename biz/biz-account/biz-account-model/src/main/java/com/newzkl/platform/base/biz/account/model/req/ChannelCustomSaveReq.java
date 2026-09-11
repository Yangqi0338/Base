package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 渠道商
 *
 * @author fang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelCustomSaveReq extends BaseReq {
    /**
     * 渠道商名称 (查询)
     * @ext 来源列 channel_name
     */
    private String name;
    /**
     * 联系方式
     */
    private String contactsWay;
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
    private String companyInfo;
    /**
     * 营业执照
     */
    private String license;
    /**
     * 数字门店权限
     */
    private CommonEnum.YesOrNo storePermission;

    /**
     * 微信 openId
     */
    private String openId;

    /**
     * 微信 unionId
     */
    private String unionId;
}
