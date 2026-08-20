package com.newzkl.platform.base.common.ddd.model.req;


import com.newzkl.platform.base.common.ddd.model.auth.OauthUserInjection;
import com.newzkl.platform.base.common.core.model.check.UpdateCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础视图实体类
 *
 * <p>标注 {@link OauthUserInjection}, 子类字段带 {@code @OauthUserId} 者随 {@code @Valid}
 * 校验自动回填当前登录用户ID。</p>
 *
 * @author god
 */
@OauthUserInjection
@Data
public class BaseReq implements Serializable {

    /**
     * id
     */
    @NotNull(groups = UpdateCommand.class)
    private Long id;

}
