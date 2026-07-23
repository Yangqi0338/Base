package com.newzkl.platform.base.biz.finance.model.event;

import com.newzkl.platform.base.biz.finance.model.earnings.req.AlterAccountContributeDataReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordReq;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author niu
 * @description: sku订单待分润数据
 * @date 2024/5/13 16:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuOrderWaitEarningVO implements Serializable {

    private Long id;

    private EarningsEnum.ConsumeType consumeType;

    private List<AlterAccountContributeDataReq> contributeDataReqs;

    private List<EarningRecordReq> earningInfos;
}
