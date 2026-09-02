package com.newzkl.platform.base.biz.goods.model.goods.vo.spu;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * sku
 * @author fang
 */
@Data
public class SkuVO extends BaseRes {
     /**
     * ID (查询)
     */
     private Long id;
     /**
      * 编码
      */
     private String code;
     /**
     * 图片
     */
     private String img;
     /**
     * 名称
     */
     @NotEmpty
     private String name;
     /**
     * 商品销售属性 SkuSaleAttributeVO.class的集合
     */
     @NotEmpty
     private List<SkuSaleAttributeVO> saleAttribute;
     /**
     * spuId (查询)
     */
     private Long spuId;
     /**
     * 市场价 (Money, 落库 BIGINT 分)
     */
     private Money marketPrice;
     /**
     * 供货价 (Money, 落库 BIGINT 分)
     */
     private Money supplyPrice;
    /**
     * 销售价(to channel) (Money, 落库 BIGINT 分)
     */
    private Money salePrice;
    /**
     * 销售价(to c) (Money, 落库 BIGINT 分)
     */
    private Money unitPrice;
     /**
      * 追加: spu状态
      */
     private Integer spuState;
     /**
      * 追加: 运费模板id (查询)
      */
     private Long freightTemplateId;
     /**
      * 追加: 账号ID (供应商)
      */
     private Long accountId;
     /**
      * 销售价加价比例
      */
     private Float salePriceRate;
     /**
      * 外部SkuId
      */
     private String outSkuId;
     /**
      * 起购数量
      */
     private Integer buyStartQty;

     /**
      * 追加: 扩展字段
      */
     private SkuExpandVO expand;
}