package com.newzkl.platform.base.biz.auth.model.oauth.dto;



import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;

import com.newzkl.platform.base.common.ddd.model.enums.auth.AuthEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录记录
 *
 * @author fang
 */
@Data
public class AccountLoginLogDTO extends BaseDTO {
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
    private AuthEnum.Type loginType;
    /**
     * 登录IP
     */
    private String loginIp;

    public void init(AuthEnum.Type loginType) {
        this.loginTime = SecurityUtils.getRequestInfo().getRequestTime();
        this.loginType = loginType;
        this.loginIp = SecurityUtils.getRequestInfo().getIp();
    }
}