package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/1217:14
 */
@Data
@AllArgsConstructor
public class IdentityRegisterRes {
    /**
     * null: 无异常
     */
    private ErrorCode errorCode;
    /**
     * 账号ID
     */
    private Long id;
    /**
     * 开发者信息
     */
    private String appId;
    /** 密钥 */
    private String secret;

    public IdentityRegisterRes(ErrorCode errorCode, Long accountId) {
        this.errorCode = errorCode;
        this.id = accountId;
    }

    public boolean isSuccess() {
        return errorCode == null;
    }
}
