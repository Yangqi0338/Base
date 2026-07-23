package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.biz.finance.model.pay.vo.CommitInfoExt;
import jakarta.validation.Valid;
import lombok.Data;

/**
 * 用户开户申请+绑卡入参
 */
@Data
public class UserApplyAccountReq<AccountReq extends UserOpenAccountReq, CardReq extends BindCardReq> {
    /**
     * 用户开户申请
     */
    @Valid
    private AccountReq accountReq;
    /**
     * 用户绑卡申请
     */
    @Valid
    private CardReq cardReq;
    /**
     * 提交的额外信息
     */
    private CommitInfoExt infoExt;
}
