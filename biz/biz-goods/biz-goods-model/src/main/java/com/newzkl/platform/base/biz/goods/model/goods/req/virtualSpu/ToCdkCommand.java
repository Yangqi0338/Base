package com.newzkl.platform.base.biz.goods.model.goods.req.virtualSpu;

import com.newzkl.platform.base.biz.goods.model.enums.user.identity.RoleEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 分配CDK参数
 * @date 2024/4/815:02
 */
@Data
public class ToCdkCommand {
    /**
     * 分配人的角色类型ID
     */
    private RoleEnum.CompanyRole fromRole;
    /**
     * 分配人给的账号ID
     */
    private Long fromUserId;
    /**
     * 被分配人的角色类型ID
     */
    @NotNull(message = "被分配人?")
    private RoleEnum.CompanyRole toRole;
    /**
     * 被分配人的账号ID
     */
    @NotNull(message = "被分配人?")
    private Long toUserId;
    /**
     * 分配的CDK ID
     */
    @NotEmpty(message = "cdkIdList?")
    private List<Long> cdkIdList;
}
