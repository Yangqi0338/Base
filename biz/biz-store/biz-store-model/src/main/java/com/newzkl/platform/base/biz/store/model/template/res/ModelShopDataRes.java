package com.newzkl.platform.base.biz.store.model.template.res;

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
    private Integer totalUseStoreNum;

    /**
     * 累计下单金额
     */
    private Integer totalOrderAmount;

    /**
     * 累计下单数
     */
    private Integer totalOrderNum;

    /**
     * 累计支付金额
     */
    private Integer totalPayAmount;

    /**
     * 累计支付订单数
     */
    private Integer totalPayNum;

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