package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;

/**
 * 连连接口公共响应参数。
 *
 * @author KC
 */
@Data
public class LianLianBaseRes {

    /**
     * 返回码, {@code 0000} 为成功。
     */
    private String ret_code;

    /**
     * 返回描述。
     */
    private String ret_msg;

    /**
     * 商户号。
     */
    private String oid_partner;

    /**
     * 是否业务成功。
     *
     * @return 返回码为 {@code 0000} 时返回 {@code true}
     */
    public boolean isSuccess() {
        return "0000".equals(ret_code);
    }
}
