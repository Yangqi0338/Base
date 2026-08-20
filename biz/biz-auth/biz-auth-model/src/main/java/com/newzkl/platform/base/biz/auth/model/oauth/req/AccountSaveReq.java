package com.newzkl.platform.base.biz.auth.model.oauth.req;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.constant.PatternConstant;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 账号注册请求参数
 *
 * @author muc_fang
 * @date 2024/2/22 11:22
 */
@Data
public class AccountSaveReq {
    /**
     * 注册角色
     */
    private AccountEnum.Identity identity;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 手机号
     */
    private String username;
    /**
     * 验证码
     */
    private String code;
    /**
     * 用户密码
     */
    @Pattern(regexp = PatternConstant.UN_CHINESE, message = "密码不能包含汉字")
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String head;
    /**
     * 父id
     */
    private Long pid;
    /**
     * 邀请人
     */
    private Long inviteId;
    /**
     * 帐号状态
     */
    private AccountEnum.State state = AccountEnum.State.ENABLE;

    /**
     * 校验账号或手机号不能同时为空
     *
     * @return 校验是否通过
     */
    @AssertTrue(message = "账号或手机号不能同时为空")
    public boolean certificateCheck() {
        return StrUtil.isBlank(phone) || StrUtil.isBlank(username);
    }

}
