package com.newzkl.platform.base.biz.account.model.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/1316:14
 */
@Data
public class ChannelOptionSyncReq implements Serializable {
    /**
     * SaaS手机账号
     */
    @NotBlank
    private String phone;
    /**
     * 期权值
     */
    @NotNull
    private Integer value;
    /**
     * 操作标识 1 增加 0 减少
     */
    @NotNull
    private Integer symbol;
    /**
     * UUID 随机唯一值, 小于128位
     */
    @NotNull
    private String uuid;
}
