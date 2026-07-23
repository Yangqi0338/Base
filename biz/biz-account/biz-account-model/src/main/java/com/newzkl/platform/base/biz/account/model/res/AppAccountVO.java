package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;

import lombok.Data;

/**
 * APP用户账号
 *
 * @author fang
 */
@Data
public class AppAccountVO extends BaseRes {
    /**
     * ALL: 手机号
     */
    private String username;
    /**
     * ALL: 姓名
     */
    private String realName;
    /**
     * ALL: 头像
     */
    private String headImg;
    /**
     * ALL: 账号角色ID集合
     */
    private String roleIdList;
    /**
     * ALL: 邀请码
     */
    private String yqm;
    /**
     * ALL: 角色id
     */
    private RoleEnum.CompanyRole role;
    /**
     * ALL: 角色名
     */
    public String getRoleName() {
        return role == null ? null : role.getValue();
    }
    /**
     * 运营商侧: 提货积分
     */
    private Integer goodsPoints;
    /**
     * 运营商侧: 等级进度
     */
    private Double levelUpProgress;
    /**
     * 运营商侧: 下级总人数
     */
    private Integer subAccountCount = 0;
    /**
     * 运营商侧: 运营商侧总人数
     */
    private Integer operatorClientCount = 0;
    /**
     * 运营商侧: 甄选师人数
     */
    private Integer selectorCount = 0;
    /**
     * 运营商侧: 交易师人数
     */
    private Integer dealerCount = 0;
    /**
     * 运营商侧: 运营商人数
     */
    private Integer operatorCount = 0;
    /**
     * 运营商侧: 渠道商人数
     */
    private Integer channelCount = 0;
    /**
     * 运营商侧: 供应商人数
     */
    private Integer supplierCount = 0;

    /**
     * ALL: 是否有店铺
     */
    private Boolean hasStore = false;

    /**
     * 用户侧: 是否是供应商
     */
    private Boolean hasChannel = false;
}