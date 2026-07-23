package com.newzkl.platform.base.biz.finance.model.purse.req.huifu;

import com.newzkl.platform.base.biz.finance.model.purse.req.BindCardReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 汇付绑卡请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HuiFuBindCardReq extends BindCardReq {

    /**
     * huifu id
     */
    private String huifuId;

}
