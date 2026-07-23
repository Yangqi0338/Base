package com.newzkl.platform.base.biz.account.model.auth.req;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.account.model.support.PatternConstant;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2211:22
 */
@Data
public class AccountSaveReq {
    /**
     * 注册角色
     */
    private RoleEnum.CompanyRole role;
    /**
     * 用户账号
     */
    private String userAccount;
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
    private String headImg;
    /**
     * 父id
     */
    private Long pid;
    /**
     * 邀请人
     */
    private Long inviteId;
    /**
     * 邀请码
     */
    private String yqm;

    /**
     * 主体类型
     *
     * @see AccountEnum.BodyType
     */
    private AccountEnum.BodyType bodyType;

    /**
     * 帐号状态（1正常 0 注销 2 禁用 - 平台禁用）
     */
    private AccountEnum.State state = AccountEnum.State.ENABLE;

    /**
     * IM同步状态（0-未同步，1-已同步，2-同步失败）
     */
    private Integer imSyncStatus;

    /**
     * IM同步错误信息（同步失败时记录，对应sys_user.im_sync_error_msg）
     */
    private String imSyncErrorMsg;

    @AssertTrue(message = "账号或手机号不能同时为空")
    public boolean certificateCheck() {
        return StrUtil.isBlank(userAccount) || StrUtil.isBlank(userAccount) || StrUtil.isBlank(username);
    }

}
