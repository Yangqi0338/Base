package com.newzkl.platform.base.biz.order.model.support.api.freight;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * @author sijiwang
 */
@Data
public class RegionRPCVO implements Serializable {

    /**
     * 1: 运费规则  2、限售区域
     */
    @NotNull
    private Integer type;
    /**
     * 是否为默认运费规则
     */
    @NotNull
    private Integer isDefault;
    /**
     * 省份
     */
    @NotEmpty
    private Set<Integer> province;

    /**
     * 市
     */
    private Set<Integer> city;

    /**
     * 区/县
     */
    private Set<Integer> region;

    /**
     * 首件
     */
    @NotNull(message = "firstPiece?")
    private BigDecimal firstPiece;

    /**
     * 首费
     */
    @NotNull
    @NotNull(message = "firstAmount?")
    private BigDecimal firstAmount;

    /**
     * 续件
     */
    @NotNull
    @NotNull(message = "secondPiece?")
    private BigDecimal secondPiece;

    /**
     * 续费
     */
    @NotNull
    @NotNull(message = "secondAmount?")
    private BigDecimal secondAmount;

    /**
     * 地区json
     */
    private String regionJson;
}
