package com.newzkl.platform.base.biz.auth.model.oauth.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.AuthEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 注册请求
 *
 * @author muc_fang
 * @date 2024/2/22 11:22
 */
@Data
public class RegisterReq {
    /**
     * 注册类型
     */
    @NotNull
    private AuthEnum.Type type;
    /**
     * 注册身份
     */
    private AccountEnum.Identity identity;
    /**
     * 用户名
     */
    @NotEmpty
    private String username;
    /**
     * 用户密码
     */
    @NotEmpty
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
    /**
     * 上级账号
     */
    private String parentId;
    /**
     * 注册后是否自动登录
     *
     * <p>默认 true: 注册成功后回登录态返回 token; false 仅注册, token 为空</p>
     */
    private Boolean login = Boolean.TRUE;
}
