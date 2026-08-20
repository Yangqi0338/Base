package com.newzkl.platform.base.biz.auth.action.cmd;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量发送短信入参
 */
@Data
public class SendVerificationCode {

    /**
     * 手机号集合
     */
    @NotEmpty
    private List<String> phone;
}
