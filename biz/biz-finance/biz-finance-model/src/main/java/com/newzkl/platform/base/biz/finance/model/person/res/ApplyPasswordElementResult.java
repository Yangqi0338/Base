package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连申请密码控件 Token 响应。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyPasswordElementResult extends LianLianBaseRes {

    /**
     * 回填的用户号 (平台打款场景回填收款方 ID)。
     */
    private String userId;

    /**
     * 用于唤起密码控件的 token。
     */
    private String password_element_token;
}
