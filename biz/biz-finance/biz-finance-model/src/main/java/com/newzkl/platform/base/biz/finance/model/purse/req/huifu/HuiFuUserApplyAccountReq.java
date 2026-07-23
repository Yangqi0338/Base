package com.newzkl.platform.base.biz.finance.model.purse.req.huifu;

import com.newzkl.platform.base.biz.finance.model.purse.req.UserApplyAccountReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 汇付企业用户开户申请+绑卡
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HuiFuUserApplyAccountReq extends UserApplyAccountReq<HuiFuUserOpenAccountReq, HuiFuBindCardReq> {

    /**
     * huifu id
     */
    private String huifuId;

}
