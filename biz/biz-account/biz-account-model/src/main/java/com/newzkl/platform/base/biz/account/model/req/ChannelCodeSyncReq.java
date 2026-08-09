package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 渠道商兑换码同步请求
 *
 * @author muc_fang
 * @date 2024/4/13 16:14
 */
@Data
public class ChannelCodeSyncReq implements Serializable {
    /**
     * SaaS手机账号
     */
    @NotBlank
    private String phone;
    /**
     * 兑换码列表
     */
    @NotEmpty
    private List<String> codeList;
}
