package com.newzkl.platform.base.biz.account.infrastructure.auth.entity;

import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 登录记录
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountLoginLogDO extends BaseIdDO {
    /**
     * 账号ID
     * @ext 查询
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
}