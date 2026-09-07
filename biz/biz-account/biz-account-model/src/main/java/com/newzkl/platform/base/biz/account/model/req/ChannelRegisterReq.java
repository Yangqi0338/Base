package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 渠道商注册请求入参
 *
 * <p>渠道商端专用注册入参, identity=CHANNEL、client=CHANNEL 由 controller 固定, 前端无需传。
 * 渠道商需指定角色 {@link #roleIdList}, 昵称必填 (同时作为渠道商名称), 登录账号空时注册链补手机号。</p>
 *
 * @author KC
 */
@Data
public class ChannelRegisterReq {

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
     * 昵称 (同时作为渠道商名称)
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
     * <p>渠道商建号后按此绑定角色, 空集视为不绑定</p>
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
