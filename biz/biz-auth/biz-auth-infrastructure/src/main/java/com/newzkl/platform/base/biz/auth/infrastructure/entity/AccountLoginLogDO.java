package com.newzkl.platform.base.biz.auth.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import com.newzkl.platform.base.common.ddd.model.enums.auth.AuthEnum;
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
@TableName
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
    private AuthEnum.Type loginType;
    /**
     * 登录IP
     */
    private String loginIp;
}