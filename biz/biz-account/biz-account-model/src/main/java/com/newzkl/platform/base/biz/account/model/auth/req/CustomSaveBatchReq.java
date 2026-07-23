package com.newzkl.platform.base.biz.account.model.auth.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2211:22
 */
@Data
public class CustomSaveBatchReq {
    /**
     * 注册角色
     */
    private List<Long> roleIdList;
    /**
     * 用户名
     */
    @NotEmpty(message = "username?")
    private String username;
    /**
     * 用户密码
     */
    @NotEmpty(message = "password?")
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 邀请码
     */
    private String yqm;
}
