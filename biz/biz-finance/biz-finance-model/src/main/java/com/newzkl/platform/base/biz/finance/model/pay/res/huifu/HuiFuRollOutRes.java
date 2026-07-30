package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;

import com.newzkl.platform.base.biz.finance.model.pay.res.TradeBaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 汇付出款回调响应
 *
 * <p>源: new-scm {@code HuiFuRollOutRes.java:43}；remark 为汇付直接返回的三方备注字段
 *
 * @author niu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HuiFuRollOutRes extends TradeBaseRes {

    /**
     * 备注（汇付三方返回字段）
     */
    private String remark;

}
