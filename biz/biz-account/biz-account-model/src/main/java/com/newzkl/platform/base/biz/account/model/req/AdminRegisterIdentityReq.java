package com.newzkl.platform.base.biz.account.model.req;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

/**
 * @author sijiwang
 */
@Data
public class AdminRegisterIdentityReq {
    /**
     * 登录账号
     */
    private String username;
    /**
     * 手机号
     */
    @Pattern(regexp = PatternUtil.MOBILE, message = "手机号格式错误")
    private String phone;
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
     * 父账号
     */
    private String superiorAccount;
    /**
     * 角色
     */
    @NotNull(message = "角色不能为空")
    private RoleEnum.CompanyRole role;
    /**
     * 后台角色id列表
     */
    private List<Long> jobIdList;
    /**
     * 账号状态 (迁移补: 批量导入会员指定启用/禁用, 源 building-old 遗漏该字段导致编译不过)
     */
    private AccountEnum.State state;

    @AssertTrue(message = "账号或手机号不能为空")
    public boolean certificateCheck() {
        return StrUtil.isBlank(username) || StrUtil.isBlank(phone);
    }
}
