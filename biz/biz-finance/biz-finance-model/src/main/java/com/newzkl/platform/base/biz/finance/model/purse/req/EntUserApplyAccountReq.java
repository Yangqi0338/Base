package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.biz.finance.model.pay.vo.CommitInfoExt;
import jakarta.validation.Valid;
import lombok.Data;

/**
 * 企业用户开户申请+绑卡入参
 */
@Data
public class EntUserApplyAccountReq<AccountReq extends EntUserOpenAccountReq, CardReq extends BindCardReq> {

    /**
     * 企业用户开户申请
     */
    @Valid
    private AccountReq accountReq;

    /**
     * 企业用户绑卡申请
     */
    @Valid
    private CardReq cardReq;

    /**
     * 提交的额外信息
     */
    private CommitInfoExt infoExt;

}
