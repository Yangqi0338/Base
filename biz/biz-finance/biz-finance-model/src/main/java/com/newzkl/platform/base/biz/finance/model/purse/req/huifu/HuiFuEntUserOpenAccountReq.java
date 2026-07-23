package com.newzkl.platform.base.biz.finance.model.purse.req.huifu;

import com.newzkl.platform.base.biz.finance.model.purse.req.EntUserOpenAccountReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 汇付企业用户开户请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HuiFuEntUserOpenAccountReq extends EntUserOpenAccountReq {

    /**
     * 汇付用户ID
     */
    private String huifuId;
}
