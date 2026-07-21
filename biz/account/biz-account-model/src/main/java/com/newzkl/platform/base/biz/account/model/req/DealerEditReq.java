package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

/**
 * 市场交易师
 *
 * @author fang
 */
@Data
public class DealerEditReq {
    /**
     * ID
     */
    private Long id;
    /**
     * 名称 (查询)
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 运营商ID
     */
    private Long operatorId;
}
