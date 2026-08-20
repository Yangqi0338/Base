package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/1217:14
 */
@Data
public class AccountRegisterRes {
    /**
     * null: 无异常
     */
    private ErrorCode errorCode;
    /**
     * 账号ID
     */
    private Long id;
    /**
     * 账号
     */
    private String username;
    /**
     * 邀请人账号ID
     */
    private Long inviteAccountId;
    /**
     * 父账号ID
     */
    private Long pid;
    /**
     * 父账号角色ID列表
     */
    private String pidList;
    /**
     * 账号身份列表
     */
    private String identityList;
    /**
     * 父账号身份列表
     */
    private String pIdentityList;


    public boolean isSuccess() {
        return errorCode == null;
    }
}
