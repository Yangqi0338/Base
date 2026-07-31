package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

@Data
public class EmpLoginParamVO {
    /** 员工ID */
    private Long id;
    /** 账号 */
    private String username;
    /** 密码 */
    private String password;
    /** 父ID */
    private Long accountId;
    /** 父账号上级ID */
    private Long upId;
    /** 类型 0 普通 1 管理员 */
    private Integer type;
}
