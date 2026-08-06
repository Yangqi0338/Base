package com.newzkl.platform.base.biz.store.model.store.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 席位套餐领域对象
 */
@Data
public class SeatPackageChannelRes implements Serializable {

    /**
     * 总席位 (数量)
     */
    private Integer totalSeatNum;

    /**
     * 已用席位 (数量)
     */
    private Integer usedSeatNum;

    /**
     * 席位列表
     */
    private List<SeatPackageResponse> seatPackageList;

    /**
     * 席位原价 (Money)
     */
    private Money seatOriginalPrice;

    /**
     * 购买最小数量
     */
    private Integer purchaseMinimumNum;

}