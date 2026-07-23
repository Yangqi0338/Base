package com.newzkl.platform.base.biz.account.model.vo;


import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import lombok.Data;

/**
 * 甄选师
 *
 * @author fang
 */
@Data
public class SelectorVO extends BaseVO {
    private RoleEnum.State state;
    /**
     * 名称
     */
    private String name;
    /**
     * 登录名称
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
     * 上级甄选师ID
     */
    private Long inviteId;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 团队人数:累加
     */
    private Integer teamCount;
    /**
     * 昨日邀请人数:定时写
     */
    private Integer yesterdayInvite;
    /**
     * 今日邀请人数:定时写
     */
    private Integer todayInvite;
    /**
     * 7日邀请人数:定时写
     */
    private Integer weekInvite;
    /**
     * 30日邀请人数:定时写
     */
    private Integer monthInvite;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 团队供应商人数:累加
     */
    private Integer teamSupplierCount;
    /**
     * 团队甄选师人数:累加
     */
    private Integer teamSelectorCount;
    /**
     * 月邀请人数:定时写
     */
    private Integer toMonthInvite;
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
     * 联表: 上级甄选师账号
     */
    private String inviteUsername;
    /**
     * 自身的订单流水
     */
    private Integer orderAmount;
    /**
     * 总订单流水
     */
    private Integer orderTotalAmount;
    /**
     * 提货积分
     */
    private Integer goodsPoints;
    /**
     * 升级进度
     */
    private Double levelUpProgress;
}