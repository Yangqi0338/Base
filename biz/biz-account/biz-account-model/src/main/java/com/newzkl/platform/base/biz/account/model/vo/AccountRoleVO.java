package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号已开通角色视图
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.rpc.model.account.AccountRoleVO}。
 * 旧实现由 {@code AccountDAO.xml#accountRoleVO} 以 supplier / channel 两表 UNION 直接取
 * {@code role_id} / {@code role_name} 冗余列; 中台身份表已去掉这两个冗余列, 故改由
 * 各身份表所属角色 (supplier 恒为供应商, channel 恒为渠道商) 推导, 取值与旧列一致。</p>
 *
 * @author KC
 */
@Data
public class AccountRoleVO implements Serializable {

    /**
     * 角色 ID
     */
    private AccountEnum.Identity identity;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 开通状态: 0 未开通 1 已开通
     */
    private Integer state;

    /**
     * 审批状态: 0 待用户提交 1 待审核 2 通过 3 未通过 4 终止
     */
    private Integer auditState;
}
