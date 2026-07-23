package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/1316:14
 */
@Data
public class ChannelCdkUseReq implements Serializable {
    /**
     * 兑换码
     */
    @NotBlank
    private String cdk;
    /**
     * 手机号
     */
    private String phone;
}
