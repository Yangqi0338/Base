package com.newzkl.platform.base.biz.store.model.template.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 样板店
 */
@Data
public class ModelShopDataRes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 使用门店数
     */
    private Integer useStoreNum;

    /**
     * 累计使用门店数
     */

    /**
     * 累计下单金额 (Money, 落库 BIGINT 分)
     */

    /**
     * 累计下单数
     */

    /**
     * 累计支付金额 (Money, 落库 BIGINT 分)
     */

    /**
     * 累计支付订单数
     */

    /**
     *  门店使用数据
     */
    private List<ModeShopDataSummary> useSummary;
    /**
     *  支付订单数
     */
    private List<ModeShopDataSummary> payOrderSummary;
    /**
     *  支付金额
     */
    private List<ModeShopDataSummary> payAmountSummary;
}