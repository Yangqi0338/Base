package com.newzkl.platform.base.biz.account.model.res;


import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录记录
 *
 * @author fang
 */
@Data
public class AccountLoginLog {

    /**
     * ID
     */
    private Long id;
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
    private AccountEnum.LoginType loginType;
    /**
     * 登录IP
     */
    private String loginIp;

    public void init(AccountVO account, AccountEnum.LoginType loginType) {
        this.accountId = account.getId();
        this.loginTime = SecurityUtils.getRequestInfo().getRequestTime();
        this.loginType = loginType;
        this.loginIp = SecurityUtils.getRequestInfo().getIp();
    }
}