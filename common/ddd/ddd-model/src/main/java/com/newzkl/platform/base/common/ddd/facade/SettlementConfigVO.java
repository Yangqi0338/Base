package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/811:47
 */
@Data
public class SettlementConfigVO implements Serializable {
    /**
     * 可结算节点:
     */
    private EarningsEnum.SettleType orderType;
    /**
     * 结算周期类型:
     */
    private EarningsEnum.DataType dataType;
    /**
     * 每月固定天数
     */
    private String dataOne;
    /**
     * 商品审核完成天数
     */
    private String dataTow;
    /**
     * 订单完成后N天结算
     */
    private Integer orderTypeDay;
    /**
     * 最小转出金额
     */
    private Money limitAmount;
}
