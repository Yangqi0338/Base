package com.newzkl.platform.base.biz.auth.model.oauth.req;


import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.auth.OauthRole;
import com.newzkl.platform.base.common.ddd.model.auth.OauthUserId;
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
    @OauthRole
    private CommonEnum.Client client;
    /**
     * 账号id
     */
    @OauthUserId
    private Long accountId;
}
