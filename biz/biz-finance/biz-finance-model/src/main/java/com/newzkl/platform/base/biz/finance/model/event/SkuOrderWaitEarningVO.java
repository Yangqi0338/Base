package com.newzkl.platform.base.biz.finance.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * SKU订单待分润数据
 *
 * <p>迁移自旧 {@code com.zkl.scm.message.rpc.model.sale.SkuOrderWaitEarningVO},
 * 作 {@code SettleSkuOrderEarningsConsumer} 消息载体</p>
 *
 * @author KC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuOrderWaitEarningVO implements Serializable {

    /**
     * SKU订单ID
     */
    private Long skuOrderId;

    /**
     * 账户贡献数据变更请求 (JSON)
     */
    private String alterAccountContributeDataReqs;

    /**
     * 分润信息 (JSON)
     */
    private String earningInfos;
}
