package com.newzkl.platform.base.biz.finance.model.person.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连绑定手机号申请请求。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BindMobileApplyReq extends TripartiteBaseParam {

    /**
     * 用户开户注册绑定手机号。
     */
    private String reg_phone;

    /**
     * 用户在商户系统中的唯一编号。
     */
    private String user_id;
}
