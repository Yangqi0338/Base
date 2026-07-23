package com.newzkl.platform.base.biz.account.model.vo;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.ChannelEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import lombok.Data;

/**
 * 渠道商
 *
 * @author fang
 */
@Data
public class ChannelVO extends BaseRes {

    /**
     * 角色ID
     */
    private RoleEnum.CompanyRole role;
    /**
     * 主体类型 (查询)
     */
    private AccountEnum.BodyType bodyType;
    /**
     * 状态 (查询)
     */
    private ChannelEnum.State state;
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
     * 企业信息
     */
    private String companyInfo;
    /**
     * 实名认证信息
     */
    private String nameAuthInfo;
    /**
     * 审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过")
     */
    private AuditEnum.State auditState;
    /**
     * 审批拒绝原因
     */
    private String auditRefuseReason;
    /**
     * 服务费配置
     */
    private String serviceFeeConfigVO;
    /**
     * 交易师收益
     */
    private Integer dealerEarnings;
    /**
     * 市场数量
     */
    private Integer marketCount;
    /**
     * 营业执照
     */
    private String license;
    /**
     * 数字门店权限 0 无 1 有
     */
    private CommonEnum.YesOrNo storePermission;
    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 店铺地址，省CODE, 6位
     */
    private Integer shipProvinceCode;
    /**
     * 店铺地址，市CODE, 6位
     */
    private Integer shipCityCode;
    /**
     * 店铺地址，区CODE, 6位
     */
    private Integer shipAreaCode;
    /**
     * 联系人名称
     */
    private String contactsName;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 总订单笔数
     */
    private Integer totalOrderNumber;
    /**
     * 总订单金额
     */
    private Integer totalOrderAmount;
    /**
     * 总售后笔数
     */
    private Integer totalRefundNumber;
    /**
     * 总售后金额
     */
    private Integer totalRefundAmount;
    /**
     * 贡献金额
     */
    private Integer contributeAmount;
    /**
     * 层级贡献金额
     */
    private String contributeAmountStr;
    /**
     * 联表:实名认证信息 : 格式:NameAuthVO
     */
    private String nameAuthVO;
    /**
     * 联表:实名认证审批状态
     */
    private AuditEnum.State nameAuthAuditState;
    /**
     * 联表:邀请码
     */
    private String yqm;
    /**
     * 联表:真实姓名
     */
    private String realName;
    /**
     * 联表:角色ID集合
     */
    private String roleIdList;
    /**
     * 联表:运营商域名
     */
    private String operatorDomain;
    /**
     * 联表:数字门店地址
     */
    private String storeUrl;
    /**
     * 联表:采购金类型 0 自营 1 合作
     */
    private Integer balanceType;
    /**
     * 渠道商贡献金额
     */
    private Integer earningContribute;
    /**
     * 联系方式
     */
    private String contactsWay;
    /**
     * 渠道商类型
     */
    private ChannelEnum.ChannelType channelType;
    /**
     * 连表: 三方账户权限
     */
    private String tripartiteAccountPermission;

    /**
     * 店铺id
     */
    public Long getStoreId() {
        return Opt.ofNullable(channelId).orElse(id);
    }

    public String getStoreUrl() {
        if (StrUtil.isNotEmpty(this.operatorDomain)) {
            if (ScmUtil.stringLast(this.operatorDomain).equals("/")) {
                return this.operatorDomain + "scm/" + this.id + "/scm/";
            } else {
                return this.operatorDomain + "/scm/" + this.id + "/scm/";
            }
        } else {
            return "";
        }
    }
}