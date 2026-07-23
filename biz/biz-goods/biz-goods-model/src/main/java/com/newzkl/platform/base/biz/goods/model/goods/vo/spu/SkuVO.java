package com.newzkl.platform.base.biz.goods.model.goods.vo.spu;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * sku
 * @author fang
 */
@Data
public class SkuVO extends BaseVO {
     /**
     * ID (查询)
     */
     private Long id;
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
     * 商品销售属性 SkuSaleAttributeVO.class的集合
     */
     @NotEmpty
     private List<SkuSaleAttributeVO> saleAttribute;
     /**
      * 补偿字段. 设计初应将 saleAttribute字段 设计为字符串
      */
     private String saleAttributeJson;
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
     @NotEmpty
     private Integer supplyPrice;
    /**
     * 销售价(to channel)
     */
    private Integer salePrice;
    /**
     * 销售价(to c)
     */
    private Integer unitPrice;
     /**
     * 库存
     */
     private Integer inventory;
     /**
     * 库存预警个数
     */
     private Integer inventoryWarning;
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
     private String expand;
}