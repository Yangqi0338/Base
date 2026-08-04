package com.newzkl.platform.base.biz.goods.model.goods.dto.spu;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuSaleAttributeVO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * sku
 * @author fang
 */
@Data
public class SkuDTO {
     /**
     * ID (查询)
     */
     private Long id;
     /**
      * 临时ID
      */
     private Long tempId;
     /**
     * 图片
     */
     private String img;
     /**
     * 条形码
     */
     private String barCode;
     /**
     * 名称
     */
     @NotEmpty
     private String name;
     /**
     * 商品销售属性
     */
     @NotEmpty
     private List<SkuSaleAttributeVO> saleAttribute;
     /**
     * 重量(千克)
     */
     private Double weight;
     /**
     * 体积(m3)
     */
     private Double volume;
     /**
     * spuId (查询)
     */
     private Long spuId;
     /**
     * 市场价
     */
     @NotEmpty
     private Integer marketPrice;
     /**
     * 供货价
     */
     private Integer supplyPrice;
     /**
      * 销售价(to channel)
      */
     private Integer salePrice;
    /**
     * 建议零售价(to c)
     */
    private Integer unitPrice;
     /**
      * 外部SkuId
      */
     private String outSkuId;
     /**
      * 销售价加价比例
      */
     private Float salePriceRate;
     /**
      * 起购数量
      */
     private Integer buyStartQty;

    /**
     * 追加: 扩展字段
     */
    private String expand;
}