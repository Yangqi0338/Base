package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

/**
 * 甄选师
 *
 * @author fang
 */
@Data
public class SelectorCustomSaveReq {
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
     * 上级甄选师ID
     */
    private Long inviteId;
    /**
     * 头像
     */
    private String headImg;
}
