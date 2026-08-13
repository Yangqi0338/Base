package com.newzkl.platform.base.biz.account.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账号信息出站出参
 *
 * <p>字段迁自旧 {@code com.zkl.scm.user.rpc.model.account.AccountInfo} 被用到的字段。</p>
 *
 * @author KC
 */
@Data
public class AccountInfoDTO implements Serializable {

    /**
     * 账号名（实为手机号）
     */
    private String username;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;
}
