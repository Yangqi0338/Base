package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

/**
 * 用户信息查询
 *
 * <p>字段名逐字沿用旧 {@code com.zkl.scm.user.domain.account.model.req.MemberInfoReq}, 不改前端契约</p>
 *
 * @author KC
 */
@Data
public class MemberInfoReq {

    /**
     * 手机号
     */
    private String phone;
}
