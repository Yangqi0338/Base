package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 汇付异步回调公共响应参数
 *
 * <p>迁移自 new-scm {@code application.utils.model.res.hf.HuiFuBaseNotifyRes}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HuiFuBaseNotifyRes extends HuiFuBaseRes {

    /**
     * 业务返回码
     */
    private String sub_resp_code;

    /**
     * 业务返回描述
     */
    private String sub_resp_desc;
}
