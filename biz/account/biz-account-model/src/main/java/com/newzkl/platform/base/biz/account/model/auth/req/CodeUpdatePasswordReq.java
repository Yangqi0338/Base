package com.newzkl.platform.base.biz.account.model.auth.req;

import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.auth.req.web.CodeUpdatePasswordCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 验证码更新密码
 *
 * @author ruoyi
 */
@Data
public class CodeUpdatePasswordReq extends CodeUpdatePasswordCommand {
    /**
     * 归属端
     */
    private CommonEnum.Client client;
    /**
     * 账号id
     */
    private Long accountId;
    /**
     * 加密字符串
     */
    @NotBlank(message = "sign不能为空")
    private String sign;
}
