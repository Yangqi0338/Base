package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

/**
 * 市场交易师
 *
 * @author fang
 */
@Data
public class DealerVO extends BaseRes {

    /** 状态 */
    private Integer state;
    /**
     * 名称 (查询)
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 角色ID
     */
    private RoleEnum.CompanyRole role;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 运营商ID
     */
    private Long operatorId;
    /**
     * 分润比例
     */
    private Double serviceRate;
    /**
     * 绑定的二级市场数量
     */
    private Integer marketCount;
    /**
     * 供应商商品数量
     */
    private Integer supplierGoodsCount;
    /**
     * 自身的订单流水
     */
    private Integer orderAmount;
    /**
     * 总订单流水
     */
    private Integer orderTotalAmount;

    /**
     * 下级渠道商数量
     */
    private Integer inviteChannelNumber;
    /**
     * 分润收益
     */
    private Integer serviceFee;
    /**
     * 提货积分
     */
    private Integer goodsPoints;
    /**
     * 升级进度
     */
    private Double levelUpProgress;
    /**
     * 联表:实名认证信息 : 格式:NameAuthVO
     */
    private String nameAuthVO;
    /**
     * 联表:实名认证审批状态
     */
    private Integer nameAuthAuditState;
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
     * 联表:运营商名称
     */
    private String operatorName;
    /**
     * 联表:运营商账号名称
     */
    private String operatorUserName;
}