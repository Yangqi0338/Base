package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 账号
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountReq extends BaseReq {
    /**
     * 登录账号
     */
    private String username;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 昵称 (查询)
     */
    private String nickname;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 手机号 (查询)
     */
    @Pattern(regexp = PatternUtil.MOBILE, message = "手机号格式错误")
    private String phone;
    /**
     * 头像
     */
    private String head;
    /**
     * 状态
     */
    private AccountEnum.State state;
    /**
     * 邀请码
     */
    private String yqm;
    /**
     * 父id
     */
    private Long pid;
    /**
     * 父id列表
     */
    private String pidList;
    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;

    /* -------------------------- 角色相关 -------------------------- */

    /**
     * 角色
     */
    @NotNull(message = "角色不能为空")
    private AccountEnum.Identity identity;
    /**
     * 后台角色id
     */
    private String jobIdList;

    /**
     * 微信绑定权限
     * @ext 1-已绑定, 0/空-未绑定; 登录/注册绑定微信时置 YES
     */
    private CommonEnum.YesOrNo wxPermission;
}
