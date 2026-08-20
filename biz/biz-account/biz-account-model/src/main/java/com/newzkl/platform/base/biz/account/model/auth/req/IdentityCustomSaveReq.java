package com.newzkl.platform.base.biz.account.model.auth.req;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 身份角色初始化请求参数(account 侧)
 *
 * <p>供 {@code AbsIdentityPolicy#customRegister} 消费, 仅承载各角色初始化所需的业务字段,
 * 不含 username/password/code 等账号注册凭证(那些属 auth 侧入参)。account 已由上游注册完成,
 * 此处只按 id 补充角色专属数据</p>
 *
 * @author muc_fang
 * @date 2024/2/22 11:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdentityCustomSaveReq extends BaseReq {

    /**
     * 是否首次注册
     *
     * <p>true: 该账号首次注册此角色, 需执行仅首次的初始化(如渠道商开发者账号初始化);
     * false: 账号已存在(如注销待回收后复用 id 覆盖注册), 跳过一次性初始化</p>
     */
    private boolean registerOnce = true;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 渠道商名称
     */
    private String name;

    /**
     * 联系方式
     */
    private String contactsWay;

    /**
     * 联系人
     */
    private String contactsName;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 营业执照
     */
    private String license;

    /**
     * 企业资质信息
     */
    private IdentitySaveReq.CompanyInfo companyInfo;

    /**
     * 数字门店权限
     */
    private CommonEnum.YesOrNo storePermission;
}
