package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 渠道商
 *
 * @author fang
 */
@Data
public class ChannelRes implements Serializable {

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
     * 角色ID
     */
    private RoleEnum.CompanyRole role;
    /**
     * 主体类型 (查询)
     */
    private Integer bodyType;
    /**
     * 状态 (查询)
     */
    private AccountEnum.State state;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 角色名称
     */
    private String roleName;
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
    private Integer auditState;
    /**
     * 审批拒绝原因
     */
    private String auditRefuseReason;
    /**
     * 服务费配置
     */
    private String serviceFeeConfigVO;
    /**
     * 交易师收益 (Money, 落库 BIGINT 分)
     */
    private Money dealerEarnings;
    /**
     * 市场数量
     */
    private Integer marketCount;
    /**
     * 营业执照
     */
    private String license;
    /**
     * 数字门店权限
     */
    private Integer storePermission;
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
     * 总订单金额 (Money, 落库 BIGINT 分)
     */
    private Money totalOrderAmount;
    /**
     * 总售后笔数
     */
    private Integer totalRefundNumber;
    /**
     * 总售后金额 (Money, 落库 BIGINT 分)
     */
    private Money totalRefundAmount;
    /**
     * 贡献金额 (Money, 落库 BIGINT 分)
     */
    private Money contributeAmount;
    /**
     * 层级贡献金额
     */
    private String contributeAmountStr;

    /**
     * 客户总数
     */
    private String customCount;

    /**
     * 联系方式
     */
    private String contactsWay;

    /**
     * 渠道商类型
     *
     * @see ChannelEnum.ChannelType
     */
    private String channelType;

    public void init() {

    }
}