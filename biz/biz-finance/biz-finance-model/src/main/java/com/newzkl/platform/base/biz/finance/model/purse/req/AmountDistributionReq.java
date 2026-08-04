package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

/**
 * @author niu
 * @description: 采购金分配请求对象
 * @date 2024/4/3 14:11
 */
@Data
public class AmountDistributionReq {

    /**
     * 金额
     */
    private Money amount;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 线下打款凭证编号，可不传，默认为0
     */
    private Long offlineRecordRemark = 0L;


}
