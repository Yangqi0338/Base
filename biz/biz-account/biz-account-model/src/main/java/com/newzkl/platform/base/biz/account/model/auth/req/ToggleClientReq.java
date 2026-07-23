package com.newzkl.platform.base.biz.account.model.auth.req;

import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2211:22
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
