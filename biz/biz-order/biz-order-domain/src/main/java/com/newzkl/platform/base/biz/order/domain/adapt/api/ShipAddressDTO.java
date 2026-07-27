package com.newzkl.platform.base.biz.order.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 收货地址
 *
 * @author KC
 */
@Data
public class ShipAddressDTO implements Serializable {

    /**
     * 收货地址ID
     */
    private Long id;

    /**
     * 所属账户ID
     */
    private Long accountId;

    /**
     * 收货联系人姓名
     */
    private String shipName;

    /**
     * 收货联系方式
     */
    private String shipPhone;

    /**
     * 省编码
     *
     * @ext 6 位行政区划编码
     */
    private Integer shipProvinceCode;

    /**
     * 省名称
     */
    private String shipProvinceName;

    /**
     * 市编码
     *
     * @ext 6 位行政区划编码
     */
    private Integer shipCityCode;

    /**
     * 市名称
     */
    private String shipCityName;

    /**
     * 区编码
     *
     * @ext 6 位行政区划编码
     */
    private Integer shipAreaCode;

    /**
     * 区名称
     */
    private String shipAreaName;

    /**
     * 详细地址
     */
    private String shipDetailAddress;

    /**
     * 邮政编码
     */
    private String shipZipCode;
}
