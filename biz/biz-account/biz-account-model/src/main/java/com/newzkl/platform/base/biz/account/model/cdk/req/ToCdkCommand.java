package com.newzkl.platform.base.biz.account.model.cdk.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 开通码分配命令。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.req.ToCdkCommand}。</p>
 *
 * @author muc_fang
 */
@Data
public class ToCdkCommand implements Serializable {

    /**
     * 分配人的角色类型 ID
     */
    private Long fromRole;

    /**
     * 分配人的账号 ID
     */
    private Long fromUserId;

    /**
     * 被分配人的角色类型 ID
     */
    @NotNull(message = "被分配人?")
    private Long toRole;

    /**
     * 被分配人的账号 ID
     */
    @NotNull(message = "被分配人?")
    private Long toUserId;

    /**
     * 待分配的开通码 ID 列表
     */
    @NotEmpty(message = "cdkIdList?")
    private List<Long> cdkIdList;
}
