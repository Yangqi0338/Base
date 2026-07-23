package com.newzkl.platform.base.biz.finance.model.purse.req.huifu;

import com.newzkl.platform.base.biz.finance.model.purse.req.EntUserApplyAccountReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 汇付企业用户开户申请+绑卡
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HuiFuEntUserApplyAccountReq extends EntUserApplyAccountReq<HuiFuEntUserOpenAccountReq, HuiFuBindCardReq> {

    /**
     * huifu id
     */
    private String huifuId;

}
