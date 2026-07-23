package com.newzkl.platform.base.biz.store.model.store.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 席位套餐领域对象
 */
@Data
public class SeatPackageChannelRes implements Serializable {

    /**
     * 总席位
     */
    private Integer totalSeatNum;

    /**
     * 已用席位
     */
    private Integer usedSeatNum;

    /**
     * 席位列表
     */
    private List<SeatPackageResponse> seatPackageList;

    /**
     * 席位原价
     */
    private Integer seatOriginalPrice;

    /**
     * 购买最小数量
     */
    private Integer purchaseMinimumNum;

}