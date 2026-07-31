package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 获取省份地区列表响应类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoAreaAddressListRes extends HuiDingHuoBaseRes<HuiDingHuoAreaAddressListRes.AddressData> {

    /**
     * 地区数据总类
     */
    @Data
    public static class AddressData {
        /**
         * 地址列表（必填）
         */
        private List<Address> addressList;

        /**
         * 版本（可选）
         */
        private String version;
    }

    /**
     * 地址信息类（递归结构）
     */
    @Data
    public static class Address {
        /**
         * 地址id（必填）
         */
        private Long id;

        /**
         * 地址名（必填）
         */
        private String areaName;

        /**
         * 地址层级（必填）
         */
        private String level;

        /**
         * 子地址（必填）
         */
        private List<Address> children;
    }
}