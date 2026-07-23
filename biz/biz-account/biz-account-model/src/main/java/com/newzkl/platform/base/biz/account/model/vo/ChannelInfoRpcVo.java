package com.newzkl.platform.base.biz.account.model.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class ChannelInfoRpcVo implements Serializable {

    /**
     * 账号ID
     */
    private Long accountId;

    /**
     * 登录名称(手机号)
     */
    private String username;

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
    private String headImg;
}
