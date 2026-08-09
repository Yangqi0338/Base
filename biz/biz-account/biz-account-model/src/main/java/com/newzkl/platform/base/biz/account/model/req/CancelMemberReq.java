package com.newzkl.platform.base.biz.account.model.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 注销会员请求参数
 *
 * @author sijiwang
 */
@Data
public class CancelMemberReq {

    /**
     * 验证码
     */
    @NotEmpty(message = "验证码不能为空")
    private String code;
}
