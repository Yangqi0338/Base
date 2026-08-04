package com.newzkl.platform.base.biz.account.model.vo;


import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 平台账号
 *
 * @author fang
 */
@Data
public class AdminAccountVO extends BaseRes {
    /**
     * 昵称 (查询)
     */
    private String nickname;
    /**
     * 头像
     */
    private String face;
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
     * 密码
     */
    private String password;
    /**
     * 帐号状态（0正常 1冻结） (查询)
     */
    private AccountEnum.State state;
}