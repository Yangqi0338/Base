package com.newzkl.platform.base.biz.account.model.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 会员注册请求参数
 *
 * <p>会员端专用注册入参: identity=MEMBER、client=USER 由 controller 固定, 前端无需传。
 * 无 jobIdList/state/username 字段, 昵称空时由后端补手机号。</p>
 *
 * @author KC
 */
@Data
public class MemberRegisterReq {

    /**
     * 手机号
     */
    @NotEmpty
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String head;

    /**
     * 父 id
     */
    private Long pid;
}
