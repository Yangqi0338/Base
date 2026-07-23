package com.newzkl.platform.base.biz.account.model.req.cmd;

import lombok.Data;

/**
 * 用户信息查询
 *
 * @author sijiwang
 */
@Data
public class MemberInfoQuery {

    /**
     * 手机号
     */
    private String phone;
}
