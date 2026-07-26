package com.newzkl.platform.base.biz.finance.model.person.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连绑定手机号验证请求。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BindMobileCheckReq extends TripartiteBaseParam {

    /**
     * 用户在商户系统中的唯一编号。
     */
    private String user_id;

    /**
     * 手机号。
     */
    private String reg_phone;

    /**
     * 手机号验证码。
     */
    private String verify_code;
}
