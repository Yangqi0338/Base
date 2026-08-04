package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.ddd.model.enums.SettleType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/811:47
 */
@Data
public class SettlementConfigOutVO implements Serializable {
    private Long id;
    /**
     * 可结算节点:
     */
    private SettleType orderType;
    /**
     * 结算周期类型:
     */
    private Integer dataType;
    /**
     * 结算周期天数: dataType = 0
     */
    private String dataOne;
    /**
     * 结算周期天数: dataType = 1
     */
    private String dataTow;

    /**
     * 订单结算类型2时，此值表示订单完成后N天结算
     */
    private Integer orderTypeDay;
}
