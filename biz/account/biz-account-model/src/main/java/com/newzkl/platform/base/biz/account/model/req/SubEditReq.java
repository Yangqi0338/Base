package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2310:23
 */
@Data
public class SubEditReq {
    private Long id;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户名
     */
    @Pattern(regexp = PatternUtil.MOBILE, message = "手机号格式错误")
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 角色ID
     */
    @NotEmpty(message = "企业角色为空")
    private List<Long> roleIdList;
    /**
     * 员工角色ID
     */
    private List<Long> jobIdList;
}
