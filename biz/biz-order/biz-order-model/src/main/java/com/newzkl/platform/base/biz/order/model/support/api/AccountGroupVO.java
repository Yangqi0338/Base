package com.newzkl.platform.base.biz.order.model.support.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 账号分组信息。
 *
 * <p>迁移: 跨域 user 结构 {@code com.zkl.scm.user.rpc.model.AccountGroupVO}
 * 降级为 order 本地 DTO。</p>
 *
 * @author KC
 */
@Data
@Accessors(chain = true)
public class AccountGroupVO implements Serializable {
    /** id。 */
    private Long id;
    /** 账号(tencent用)。 */
    private String userAccount;
    /** 昵称。 */
    private String nickname;
    /** 头像。 */
    private String head;
    /** 手机号。 */
    private String phone;
    /** 帐号状态（0用户注销 1正常 -1平台禁用） (查询)。 */
    private Integer state;
}
