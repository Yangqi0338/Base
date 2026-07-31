package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 获取运费模板详细信息响应类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetExpressFeeTemplatesRes extends HuiDingHuoBaseRes<List<HuiDingHuoGetExpressFeeTemplatesRes.FeeTemplate>> {

    /**
     * 运费模板信息类
     */
    @Data
    public static class FeeTemplate {
        /**
         * 运费模板编号（必填）
         */
        private String id;

        /**
         * 不可送达地区（可选）
         */
        private UndeliverableArea undeliverableArea;

        /**
         * 模板名称（必填）
         */
        private String templateName;

        /**
         * 计价方式（0：按金额，1：按件，2：按重量，必填）
         */
        private Integer valuationModel;

        /**
         * 计价方式名称（必填）
         */
        private String valuationModelName;

        /**
         * 运费方式按金额（与carryMode二选一）
         */
        private CarryModeAmount carryModeAmount;

        /**
         * 运费方式按数量/重量（与carryModeAmount二选一）
         */
        private CarryMode carryMode;
    }

    /**
     * 不可送达地区类
     */
    @Data
    public static class UndeliverableArea {
        private List<Region> regions; // 地区列表（可选）
    }

    /**
     * 地区信息类（递归结构）
     */
    @Data
    public static class Region {
        private String name; // 地区名字（可选）
        private List<Region> childAreaList; // 子地区（可选）
    }

    /**
     * 运费方式按金额类
     */
    @Data
    public static class CarryModeAmount {
        private DefaultCarryMode defaultCarryMode; // 默认运费方式（必填）
        private List<CarryModeItem> carryModeList; // 特殊运费方式（可选）
    }

    /**
     * 运费方式按数量/重量类
     */
    @Data
    public static class CarryMode {
        private DefaultCarryMode defaultCarryMode; // 默认运费方式（必填）
        private List<CarryModeItem> carryModeList; // 特殊运费方式（可选）
    }

    /**
     * 默认运费方式类
     */
    @Data
    public static class DefaultCarryMode {
        private BigDecimal firstLimit; // 限制值（必填）
        private BigDecimal firstPrice; // 首价（必填）
        private BigDecimal secondLimit; // 续件/续重（可选）
        private BigDecimal secondPrice; // 续价（可选）
        private List<Region> regions; // 地区（默认运费方式下为空，可选）
    }

    /**
     * 特殊运费方式项
     */
    @Data
    public static class CarryModeItem {
        private BigDecimal firstLimit; // 限制值（必填）
        private BigDecimal firstPrice; // 首价（必填）
        private BigDecimal secondLimit; // 续件/续重（可选）
        private BigDecimal secondPrice; // 续价（可选）
        private List<Region> regions; // 地区（特殊运费方式下不为空，必填）
    }
}