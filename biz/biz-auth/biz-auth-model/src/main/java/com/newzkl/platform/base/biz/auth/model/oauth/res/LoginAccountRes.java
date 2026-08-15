package com.newzkl.platform.base.biz.auth.model.oauth.res;


import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 登录账号结果
 *
 * @author fang
 */
@Data
public class LoginAccountRes extends BaseRes {
    /**
     * 昵称 (查询)
     */
    private String nickname;
    /**
     * 手机号 (查询)
     */
    @Pattern(regexp = PatternUtil.MOBILE, message = "手机号格式错误")
    private String phone;
    /**
     * 登录名称(查询)
     */
    private String username;
    /**
     * 头像
     */
    private String head;
    /**
     * 邀请码
     */
    private String yqm;
    /**
     * 帐号状态
     */
    private AccountEnum.State state;
}