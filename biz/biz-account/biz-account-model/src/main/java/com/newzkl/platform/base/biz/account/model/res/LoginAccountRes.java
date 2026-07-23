package com.newzkl.platform.base.biz.account.model.res;


import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.vo.AccountJobVO;
import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

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
     * 登录账号
     */
    private String userAccount;
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
    /**
     * 岗位ID
     */
    private List<AccountJobVO> jobList;

}