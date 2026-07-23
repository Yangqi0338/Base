package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

/**
 * 甄选师
 *
 * @author fang
 */
@Data
public class SelectorEditReq {
    /**
     * ID
     */
    private Long id;
    /**
     * 名称 (查询)
     */
    private String name;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 上级甄选师ID
     */
    private Long inviteId;
    /**
     * 头像
     */
    private String headImg;
}
