package com.newzkl.platform.base.biz.auth.model.oauth.res;


import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
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
    /**
     * 三方账户权限
     *
     * <p>前端据此判断是否需进入汇付开卡流程, 替代已移除的 GET /huiFu/detail 是否为 null 的判断。</p>
     */
    private CommonEnum.YesOrNo tripartiteAccountPermission;
}