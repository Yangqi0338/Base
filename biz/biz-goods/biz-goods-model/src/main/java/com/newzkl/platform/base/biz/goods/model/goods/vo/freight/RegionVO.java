package com.newzkl.platform.base.biz.goods.model.goods.vo.freight;

import com.newzkl.platform.base.common.core.model.money.Money;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @Description: 地区信息
 * @Author: niu
 * @Date: 2023/4/27 14:48
 */
@Data
public class RegionVO {

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
    @NotNull
    private BigDecimal firstPiece;

    /**
     * 首费
     */
    @NotNull
    private Money firstAmount;

    /**
     * 续件
     */
    @NotNull
    private BigDecimal secondPiece;

    /**
     * 续费
     */
    @NotNull
    private Money secondAmount;

    /**
     * 地区json
     */
    private String regionJson;
}
