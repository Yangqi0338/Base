package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
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
public class ChannelCustomSaveReq implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 上级交易师ID
     */
    private Long upDealerId;
    /**
     * 上级运营商ID
     */
    private Long upOperatorId;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 渠道商名称 (查询) channel_name
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;

    /**
     * 主体类型

     */
    private AccountEnum.BodyType bodyType;

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
     * 渠道商类型
     */
    private ChannelEnum.ChannelType channelType;

    /**
     * 数字门店权限
     */
    private CommonEnum.YesOrNo storePermission;

}
