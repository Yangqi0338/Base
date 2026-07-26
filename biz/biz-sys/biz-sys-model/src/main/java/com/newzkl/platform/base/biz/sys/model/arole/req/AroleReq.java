package com.newzkl.platform.base.biz.sys.model.arole.req;

import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 后台角色请求对象。
 *
 * @author KC
 */
@Data
public class AroleReq {

    /**
     * 角色 id (更新时必填)。
     */
    @NotNull(groups = UpdateCommand.class)
    private Long id;

    /**
     * 角色名称。
     */
    @NotBlank
    private String name;

    /**
     * 备注。
     */
    private String comment;
}
