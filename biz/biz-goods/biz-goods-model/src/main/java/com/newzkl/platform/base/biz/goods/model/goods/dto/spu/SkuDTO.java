package com.newzkl.platform.base.biz.goods.model.goods.dto.spu;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuExpandVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuSaleAttributeVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * sku
 *
 * <p>继承 {@code BaseDTO} 承接 id/creatorId/executor/createTime/updateTime,
 * 使 DO → DTO → VO 两跳转换不丢审计字段</p>
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SkuDTO extends BaseDTO {
     /**
      * 编码 (后端生成, 不接受入参)
      */
     private String code;
     /**
      * 临时ID
      */
     private Long tempId;
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
     * 商品销售属性
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
     * 建议零售价(to c) (Money, 落库 BIGINT 分)
     */
    private Money unitPrice;
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
    private SkuExpandVO expand;
}