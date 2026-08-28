package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 员工注册请求参数
 *
 * <p>员工端专用注册入参: identity=EMP、client=ADMIN 由 controller 固定, 前端无需传。
 * 员工需指定角色 {@link #roleIdList}, 昵称必填, 登录账号空时注册链补手机号。</p>
 *
 * @author KC
 */
@Data
public class EmpRegisterReq {

    /**
     * 登录账号
     *
     * <p>空时注册链补手机号</p>
     */
    private String username;

    /**
     * 手机号
     */
    @NotEmpty
    private String phone;

    /**
     * 昵称
     */
    @NotEmpty
    private String nickname;

    /**
     * 头像
     */
    private String head;

    /**
     * 角色 id 集合
     *
     * <p>员工建号后按此绑定角色, 空集视为不绑定</p>
     */
    private List<Long> roleIdList;

    /**
     * 父 id
     */
    private Long pid;

    /**
     * 账号状态
     */
    private AccountEnum.State state = AccountEnum.State.ENABLE;
}
