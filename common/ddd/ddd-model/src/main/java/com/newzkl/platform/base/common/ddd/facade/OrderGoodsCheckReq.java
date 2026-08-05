package com.newzkl.platform.base.common.ddd.facade;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: niu
 * @Date: 2022/5/7 16:52
 */
@Data
@Accessors(chain = true)
public class OrderGoodsCheckReq implements Serializable {

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 收货方式 0:物流 1：自提
     */
    private Integer distributionMode;

    /**
     * 收货地址编码，省CODE, 6位
     */
    private Integer shipProvinceCode;
    /**
     * 收货地址编码，市CODE, 6位
     */
    private Integer shipCityCode;
    /**
     * 收货地址编码，区CODE, 6位
     */
    private Integer shipAreaCode;

    /**
     * 收货地区，例如:辽宁省,沈阳市,铁西区,XXX镇
     */
    private String shipArea;
}
