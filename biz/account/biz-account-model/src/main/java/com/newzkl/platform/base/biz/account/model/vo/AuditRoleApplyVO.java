package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import lombok.Data;

/**
 * @author muc_fang表
 * @author fang
 */
@Data
public class AuditRoleApplyVO extends BaseVO {
    /**
     * 账号ID
     */
    private Long accountId;
    /**
     * 审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过",4,"终止")
     */
    private AuditEnum.State state;
    /**
     * 注册手机号
     */
    private String registerPhone;
    /**
     * 申请角色
     */
    private RoleEnum.CompanyRole role;
    /**
     * 主体类型
     */
    private AccountEnum.BodyType bodyType;
    /**
     * 法人姓名
     */
    private String legalName;
    /**
     * 负责人姓名
     */
    private String managerName;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 邀请账号
     */
    private String inviteAccount;
    /**
     * 邀请人昵称
     */
    private String inviteNickName;
    private String companyInfo;
    private String nameAuthInfo;

    /**
     * 最后拒绝原因: 状态变更未待用户提交前的最后一次拒绝原因
     */
    private String lastRefuseReason;
    /**
     * 申请人账号
     */
    private String username;
}