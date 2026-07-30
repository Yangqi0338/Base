package com.newzkl.platform.base.biz.sys.model.adminaccount.req;

import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 平台账号请求对象
 *
 * @author KC
 */
@Data
public class AdminAccountReq {

    /**
     * 账号 id (更新时必填)
     */
    @NotNull(groups = UpdateCommand.class)
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String face;

    /**
     * 手机号
     */
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;

    /**
     * 登录名称
     */
    private String username;

    /**
     * 明文密码 (领域层负责 BCrypt 加密后落库)
     */
    private String password;

    /**
     * 帐号状态 (0 正常 1 冻结)
     */
    private Integer state;

    /**
     * 后台角色 id 列表 (JSON/逗号串)
     */
    private String aroleIdList;
}
