package com.newzkl.platform.base.biz.auth.model.oauth.req;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 切换登录端请求参数
 *
 * @author muc_fang
 * @date 2024/2/22 11:22
 */
@Data
public class ToggleClientReq {
    /**
     * 端
     */
    @NotNull(message = "端不能为空")
    private CommonEnum.Client client;

    /**
     * 账号id
     */
    @NotNull(message = "账号id不能为空")
    private Long accountId;

}
