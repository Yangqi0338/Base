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