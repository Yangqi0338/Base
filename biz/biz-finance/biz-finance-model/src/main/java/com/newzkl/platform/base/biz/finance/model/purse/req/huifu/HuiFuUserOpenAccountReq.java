package com.newzkl.platform.base.biz.finance.model.purse.req.huifu;

import com.newzkl.platform.base.biz.finance.model.purse.req.UserOpenAccountReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 汇付用户开户请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HuiFuUserOpenAccountReq extends UserOpenAccountReq {

    /**
     * 汇付用户ID
     */
    private String huifuId;

}
