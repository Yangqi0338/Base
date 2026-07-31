package com.newzkl.platform.base.biz.account.model.req.web;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * 甄选师
 *
 * @author fang
 */
@Data
public class SelectorProxySaveReq implements Serializable {
    /** 账号 */
    @NotEmpty(message = "username?")
    private String username;
    /** 密码 */
    @NotEmpty(message = "password?")
    private String password;
    /**
     * 名称 (查询)
     */
    @NotEmpty(message = "name?")
    private String name;
    /**
     * 手机号
     */
    @NotEmpty(message = "手机号?")
    private String phone;
    /**
     * 运营商邀请码
     */
/** @NotEmpty(message = "yqm?") */
    private String yqm;
}
