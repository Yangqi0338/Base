package com.newzkl.platform.base.biz.account.model.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 渠道商期权值同步请求
 *
 * @author muc_fang
 * @date 2024/4/13 16:14
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
     * 操作标识
     * @ext 1 增加 0 减少; 无对应枚举, 保留 Integer
     */
    @NotNull
    private Integer symbol;
    /**
     * UUID 随机唯一值
     * @ext 小于 128 位
     */
    @NotNull
    private String uuid;
}
