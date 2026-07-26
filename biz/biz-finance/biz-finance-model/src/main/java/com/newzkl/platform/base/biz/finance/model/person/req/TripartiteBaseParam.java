package com.newzkl.platform.base.biz.finance.model.person.req;

import lombok.Data;

/**
 * 连连三方接口公共请求参数。
 *
 * <p>字段名为下划线风格, 与连连报文一一对应, 不做驼峰改写。</p>
 *
 * @author KC
 */
@Data
public class TripartiteBaseParam {

    /**
     * 时间戳, 格式 yyyyMMddHHmmss。
     */
    private String timestamp;

    /**
     * 商户号。
     */
    private String oid_partner;
}
