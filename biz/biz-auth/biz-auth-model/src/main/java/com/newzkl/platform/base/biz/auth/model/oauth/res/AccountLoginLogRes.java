package com.newzkl.platform.base.biz.auth.model.oauth.res;


import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录记录
 *
 * @author fang
 */
@Data
public class AccountLoginLogRes extends BaseRes {
    /**
     * 账号ID (查询)
     */
    private Long accountId;
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 登录方式
     */
    private Integer loginType;
    /**
     * 登录IP
     */
    private String loginIp;
}