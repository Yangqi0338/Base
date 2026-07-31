package com.newzkl.platform.base.biz.finance.model.earnings.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2024/5/13 15:52
 */
@Data
public class SettleEarningReq implements Serializable {

    /**
     * sku订单id
     */
    private Long skuOrderId;

    /** 修改账户贡献数据请求列表 */
    private List<AlterAccountContributeDataReq> alterAccountContributeDataReqs;

    /** 收益信息列表 */
    private List<EarningRecordReq> earningInfos;
}
