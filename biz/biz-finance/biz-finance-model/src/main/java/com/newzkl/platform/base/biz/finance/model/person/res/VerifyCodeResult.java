package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连绑定手机验证码申请/验证响应。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyCodeResult extends LianLianBaseRes {

    /**
     * 用户在商户系统中的唯一编号。
     */
    private String user_id;

    /**
     * 注册手机号。
     */
    private String reg_phone;
}
