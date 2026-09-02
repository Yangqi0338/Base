package com.newzkl.platform.base.biz.goods.model.goods.vo.spu;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * spu 扩展字段
 *
 * <p>对应 {@code spu.expand} JSON 列, 承载由分类/品牌/账号/SKU 聚合而来的冗余字段
 * 全链 (DO/DTO/VO) 统一持本对象直传, 不做 String 与对象之间的手写互转</p>
 *
 * @author KC
 */
@Data
public class SpuExpandVO implements Serializable {
    /**
     * 所属平台分类名称完整
     */
    private String categoryName;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 账号名称
     */
    private String accountName;
    /**
     * 市场价起始
     */
    private Money marketPriceBegan;
    /**
     * 市场价结束
     */
    private Money marketPriceEnd;
    /**
     * 销售价起始
     */
    private Money salePriceBegan;
    /**
     * 销售价结束
     */
    private Money salePriceEnd;
    /**
     * 供货价起始
     */
    private Money supplierPriceBegan;
    /**
     * 供货价结束
     */
    private Money supplierPriceEnd;
}
