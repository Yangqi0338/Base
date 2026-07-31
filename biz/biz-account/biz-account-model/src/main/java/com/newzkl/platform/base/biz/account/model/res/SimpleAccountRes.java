package com.newzkl.platform.base.biz.account.model.res;

import lombok.Data;

@Data
public class SimpleAccountRes {
    /** 账户ID */
    private Long id;
    /** 账号 */
    private String username;
    /** 真实姓名 */
    private String realName;
}
