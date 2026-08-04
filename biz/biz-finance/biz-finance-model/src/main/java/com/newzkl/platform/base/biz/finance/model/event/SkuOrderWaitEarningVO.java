package com.newzkl.platform.base.biz.finance.model.event;

import com.newzkl.platform.base.biz.finance.model.earnings.req.AlterAccountContributeDataReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordReq;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
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

    /** 主键ID */
    private Long id;

    /** 消费类型 */
    private EarningsEnum.ConsumeType consumeType;

    /** 贡献数据请求列表 */
    private List<AlterAccountContributeDataReq> contributeDataReqs;

    /** 收益信息列表 */
    private List<EarningRecordReq> earningInfos;
}
