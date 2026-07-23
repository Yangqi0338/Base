package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.ChannelEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 渠道商
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelReq extends BaseReq {
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 状态
     */
    private ChannelEnum.State state;
    /**
     * 渠道商名称 (查询) channel_name
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 审批状态
     */
    private AuditEnum.State auditState;
    /**
     * 营业执照
     */
    private String license;

    /**
     * 企业信息
     */
    private String companyInfo;

    /**
     * 店铺权限
     */
    private CommonEnum.YesOrNo storePermission;

    /**
     * 联系人姓名
     */
    private String contactsName;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 联系人方式
     */
    private String contactsWay;

}
