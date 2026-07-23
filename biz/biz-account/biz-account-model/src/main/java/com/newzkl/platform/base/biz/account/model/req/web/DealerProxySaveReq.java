package com.newzkl.platform.base.biz.account.model.req.web;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * 交易师
 *
 * @author fang
 */
@Data
public class DealerProxySaveReq implements Serializable {
    @NotEmpty(message = "username?")
    private String username;
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
    private Long phone;
    /**
     * 运营商邀请码
     */
//    @NotEmpty(message = "yqm?")
    private String yqm;
    /**
     * 分润比例
     */
    private Double serviceRate;
}
