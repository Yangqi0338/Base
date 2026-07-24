package com.newzkl.platform.base.biz.account.infrastructure.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.AuditBaseDO;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
// TODO[pom-gap mybatis-plus-ext]: import org.dromara.mpe.autofill.annotation.JsonSerializable; (parent 已 depMgmt mybatis-plus-ext, infra 需补依赖)

/**
 * 数据角色申请审核数据实体类
 * <p>
 * 用于存储用户申请数据角色时的审核相关信息，
 * 包括账户信息、角色类型、身份认证信息以及邀请人信息等。
 * </p>
 *
 * @author system
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class AuditDataRoleApplyDO extends AuditBaseDO {

    /**
     * 注册手机号
     */
    private String registerPhone;

    /**
     * 公司角色类型
     */
    @Index
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
     * 管理员姓名
     */
    private String managerName;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 邀请人账户
     */
    private String inviteAccount;

    /**
     * 邀请人昵称
     */
    private String inviteNickName;

    /**
     * 公司详细信息
     */
    // TODO[pom-gap mybatis-plus-ext]: @JsonSerializable (autofill, parent 已 depMgmt mybatis-plus-ext, infra 需补依赖)
    private String companyInfo;

    /**
     * 实名认证信息
     */
    // TODO[pom-gap mybatis-plus-ext]: @JsonSerializable (autofill, parent 已 depMgmt mybatis-plus-ext, infra 需补依赖)
    private String nameAuthInfo;
}